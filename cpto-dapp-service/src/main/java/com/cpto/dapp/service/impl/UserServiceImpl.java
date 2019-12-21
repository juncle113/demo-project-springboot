package com.cpto.dapp.service.impl;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.common.helper.CsvHelper;
import com.cpto.dapp.common.util.*;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.constant.SettingsConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.SystemSettings;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.enums.ErrorEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.enums.VerifyCaseEnum;
import com.cpto.dapp.enums.VerifyTypeEnum;
import com.cpto.dapp.exception.*;
import com.cpto.dapp.pojo.dto.*;
import com.cpto.dapp.pojo.vo.InviteVO;
import com.cpto.dapp.pojo.vo.LoginVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.UserVO;
import com.cpto.dapp.repository.UserRepository;
import com.cpto.dapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户ServiceImpl
 *
 * @author sunli
 * @date 2018/12/29
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemSettingsService systemSettingsService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AuthManager authManager;

    @Autowired
    private CsvHelper csvHelper;

    @Value("${custom.other.invite-reward-times}")
    private Integer inviteRewardTimes;

    /**
     * 注册新的用户
     *
     * @param registerDTO 注册信息
     */
    @Override
    public void register(RegisterDTO registerDTO) {

        /* 1.检查用户输入信息 */
        checkUserInputInfo(registerDTO);

        /* 2.检查邀请码 */
        Long parentId = null;
        if (ObjectUtil.isNotEmpty(registerDTO.getInviteCode())) {
            User parent = findByInviteCode(registerDTO.getInviteCode());
            parentId = parent.getId();
        }

        /* 3.检查验证码 */
        // 设置验证信息
        User userVerifyInfo = new User();
        userVerifyInfo.setAreaCode(registerDTO.getAreaCode());
        userVerifyInfo.setPhone(registerDTO.getPhone());
        userVerifyInfo.setEmail(registerDTO.getEmail());
        codeService.verifyCode(VerifyCaseEnum.REGISTER.getCode(), getSendTo(userVerifyInfo, registerDTO.getVerifyType()), registerDTO.getCode());

        /* 4.创建用户信息 */
        User user = createUser(registerDTO, parentId);

        /* 5.初始化用户账户信息 */
        accountService.initAccount(user.getId());

        /* 6.初始化用户钱包信息 */
        walletService.initWallet(user.getId());
    }

    /**
     * 登录处理
     *
     * @param loginDTO 登录信息
     * @return 登录信息
     */
    @Override
    public LoginVO login(LoginDTO loginDTO) {

        /* 1.根据登录账号查询用户信息 */
        User user = findUserByAccount(loginDTO.getAccount());

        /* 2.通过MD5加密登录密码 */
        String md5Password = StringUtil.toMd5(loginDTO.getPassword());

        /* 3.进行登录检查 */
        // 登录失败：用户名或密码错误的场合
        if (ObjectUtil.isEmpty(user) || ObjectUtil.notEquals(md5Password, user.getPassword())) {
            throw new LoginException();
        }

        // 登录失败：账号被禁用的场合
        if (ObjectUtil.equals(user.getStatus(), StatusEnum.INVALID.getCode())) {
            throw new AuthorizedException();
        }

        /* 4.创建token，并缓存 */
        String token = authManager.createToken(user.getId());

        /* 5.登录成功，返回登录信息 */
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);

        return loginVO;
    }

    /**
     * 注销处理
     *
     * @param user 用户
     */
    @Override
    public void logout(User user) {
        // 清除缓存中的token
        authManager.removeToken(user.getId());
    }

    /**
     * 查询满足条件的用户
     *
     * @param searchTime      查询时间
     * @param page            当前页数
     * @param pageSize        每页条数
     * @param id              用户id
     * @param userName        用户名
     * @param areaCode        手机号归属地代码
     * @param phone           手机号
     * @param email           邮箱
     * @param isSubscribeMail 是否订阅
     * @param inviteCode      邀请码
     * @param parentId        上级邀请人
     * @param status          状态
     * @param remark          备注
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 用户列表
     */
    @Override
    public PageVO<UserVO> searchUser(Timestamp searchTime,
                                     Integer page,
                                     Integer pageSize,
                                     Long id,
                                     String userName,
                                     String areaCode,
                                     String phone,
                                     String email,
                                     Boolean isSubscribeMail,
                                     String inviteCode,
                                     Long parentId,
                                     Integer status,
                                     String remark,
                                     String fromCreatedTime,
                                     String toCreatedTime) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<User> specification = getSQLWhere(searchTime, id, userName, areaCode, phone, email, isSubscribeMail, inviteCode, parentId, status, remark, fromCreatedTime, toCreatedTime);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<User> userPage = userRepository.findAll(specification, pageable);

        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userPage) {
            userVOList.add(editUserVO(user));
        }

        PageVO<UserVO> userPageVO = new PageVO();
        userPageVO.setRows(userVOList);
        userPageVO.setTotal(userPage.getTotalElements());
        userPageVO.setTotalPage(userPage.getTotalPages());
        userPageVO.setHasNext(userPage.hasNext());
        userPageVO.setSearchTime(searchTime);

        return userPageVO;
    }

    /**
     * 根据id取得用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public UserVO findUser(Long userId) {
        User user = findUserById(userId);
        return editUserVO(user);
    }

    /**
     * 根据id取得用户信息
     *
     * @param user 用户
     * @return 用户信息
     */
    @Override
    public UserVO findUser(User user) {
        return editUserVO(user);
    }

    /**
     * 根据id取得用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public User getUser(Long userId) {
        return findUserById(userId);
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime      查询时间
     * @param id              用户id
     * @param userName        用户名
     * @param areaCode        手机号归属地代码
     * @param phone           手机号
     * @param email           邮箱
     * @param isSubscribeMail 是否订阅
     * @param inviteCode      邀请码
     * @param parentId        上级邀请人
     * @param status          状态
     * @param remark          备注
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 动态查询条件
     */
    private Specification<User> getSQLWhere(Timestamp searchTime,
                                            Long id,
                                            String userName,
                                            String areaCode,
                                            String phone,
                                            String email,
                                            Boolean isSubscribeMail,
                                            String inviteCode,
                                            Long parentId,
                                            Integer status,
                                            String remark,
                                            String fromCreatedTime,
                                            String toCreatedTime) {

        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询用户id
                if (ObjectUtil.isNotEmpty(id)) {
                    predicatesList.add(cb.equal(root.get("id"), id));
                }

                // 模糊查询用户名
                if (ObjectUtil.isNotEmpty(userName)) {
                    predicatesList.add(cb.like(cb.lower(root.get("userName")), "%" + userName.toLowerCase() + "%"));
                }

                // 精确查询归属地
                if (ObjectUtil.isNotEmpty(areaCode)) {
                    predicatesList.add(cb.equal(root.get("areaCode"), areaCode));
                }

                // 模糊查询手机号
                if (ObjectUtil.isNotEmpty(phone)) {
                    predicatesList.add(cb.like(root.get("phone"), "%" + phone + "%"));
                }

                // 模糊查询邮箱
                if (ObjectUtil.isNotEmpty(email)) {
                    predicatesList.add(cb.like(cb.lower(root.get("email")), "%" + email + "%"));
                }

                // 精确查询是否订阅
                if (ObjectUtil.isNotEmpty(isSubscribeMail)) {
                    predicatesList.add(cb.equal(root.get("isSubscribeMail"), isSubscribeMail));
                }

                // 精确查询邀请码
                if (ObjectUtil.isNotEmpty(inviteCode)) {
                    predicatesList.add(cb.equal(cb.lower(root.get("inviteCode")), inviteCode.toLowerCase()));
                }

                // 精确查询上级邀请人id
                if (ObjectUtil.isNotEmpty(parentId)) {
                    predicatesList.add(cb.equal(root.get("parentId"), parentId));
                }

                // 精确查询用户状态
                if (ObjectUtil.isNotEmpty(status)) {
                    predicatesList.add(cb.equal(root.get("status"), status));
                }

                // 模糊查询备注
                if (ObjectUtil.isNotEmpty(remark)) {
                    predicatesList.add(cb.like(cb.lower(root.get("remark")), "%" + remark.toLowerCase() + "%"));
                }

                // 范围查询创建时间
                if (ObjectUtil.isNotEmpty(fromCreatedTime)) {
                    predicatesList.add(cb.greaterThanOrEqualTo(root.get("createdTime"), Timestamp.valueOf(DateUtil.fullFromTime(fromCreatedTime))));
                }
                if (ObjectUtil.isNotEmpty(toCreatedTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), Timestamp.valueOf(DateUtil.fullToTime(toCreatedTime))));
                }

                // 返回生成的条件（条件为并且的关系）
                return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
            }
        };

        return specification;
    }

    /**
     * 检查用户输入信息
     *
     * @param registerDTO 新建的用户信息
     */
    private void checkUserInputInfo(RegisterDTO registerDTO) {

        /* 1.检查用户名是否存在 */
        existsUserName(registerDTO.getUserName());

        // 手机验证的场合
        if (ObjectUtil.equals(registerDTO.getVerifyType(), VerifyTypeEnum.PHONE.getCode())) {
            /* 2.检查手机号是否存在 */
            existsPhone(registerDTO.getAreaCode(), registerDTO.getPhone());
        }
        // 邮箱验证的场合
        else {
            /* 3.检查邮箱是否存在 */
            existsEmail(registerDTO.getEmail());
        }
    }

    /**
     * 创建用户信息
     *
     * @param registerDTO 新建的用户信息
     * @param parentId    上级推荐人id
     * @return 用户信息
     */
    private User createUser(RegisterDTO registerDTO, Long parentId) {

        User user = new User();

        if (ObjectUtil.equals(registerDTO.getVerifyType(), VerifyTypeEnum.PHONE.getCode())) {
            user.setAreaCode(registerDTO.getAreaCode());
            user.setPhone(registerDTO.getPhone());
        } else {
            user.setEmail(registerDTO.getEmail());
        }

        user.setId(IdUtil.generateIdByCurrentTime());
        user.setUserName(registerDTO.getUserName());
        user.setPassword(StringUtil.toMd5(registerDTO.getPassword()));
        user.setParentId(parentId);
        user.setIsSubscribeMail(false);
        user.setStatus(StatusEnum.VALID.getCode());
        user.setCreatedBy(null);
        user.setCreatedTime(DateUtil.now());

        // 生成邀请码
        String inviteCode;
        boolean existsByInviteCode;
        do {
            inviteCode = IdUtil.generateCode(8);
            existsByInviteCode = userRepository.existsByInviteCode(inviteCode);
        } while (existsByInviteCode);

        user.setInviteCode(inviteCode);

        user = userRepository.save(user);

        return user;
    }

    /**
     * 修改登录密码
     *
     * @param user              用户
     * @param modifyPasswordDTO 修改登录密码信息
     */
    @Override
    public void modifyPassword(User user, ModifyPasswordDTO modifyPasswordDTO) {
        setPassword(user, modifyPasswordDTO.getVerifyType(), modifyPasswordDTO.getCode(), modifyPasswordDTO.getPassword());
    }

    /**
     * 重置登录密码
     *
     * @param resetPasswordDTO 重置登录密码信息
     */
    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {

        /* 1.取得用户信息 */
        User user = findUserByVerifyInfo(resetPasswordDTO.getVerifyType(), resetPasswordDTO.getAreaCode(), resetPasswordDTO.getPhone(), resetPasswordDTO.getEmail());

        /* 2.设置登录密码 */
        setPassword(user, resetPasswordDTO.getVerifyType(), resetPasswordDTO.getCode(), resetPasswordDTO.getPassword());
    }

    /**
     * 设置登录密码
     *
     * @param user       用户信息
     * @param verifyType 验证方式
     * @param code       验证码
     * @param password   登录密码
     */
    private void setPassword(User user, Integer verifyType, String code, String password) {

        /* 1.检查验证码 */
        codeService.verifyCode(VerifyCaseEnum.PASSWORD.getCode(), getSendTo(user, verifyType), code);

        /* 2.修改登录密码 */
        user.setPassword(StringUtil.toMd5(password));
        user.setModifiedBy(null);
        user.setModifiedTime(DateUtil.now());
        userRepository.save(user);
    }

    /**
     * 修改支付密码
     *
     * @param user                 用户
     * @param modifyPayPasswordDTO 修改支付密码信息
     */
    @Override
    public void modifyPayPassword(User user, ModifyPayPasswordDTO modifyPayPasswordDTO) {
        setPayPassword(user, modifyPayPasswordDTO.getVerifyType(), modifyPayPasswordDTO.getCode(), modifyPayPasswordDTO.getPayPassword());
    }

    /**
     * 重置支付密码
     *
     * @param resetPayPasswordDTO 重置支付密码信息
     */
    @Override
    public void resetPayPassword(ResetPayPasswordDTO resetPayPasswordDTO) {

        /* 1.取得用户信息 */
        User user = findUserByVerifyInfo(resetPayPasswordDTO.getVerifyType(), resetPayPasswordDTO.getAreaCode(), resetPayPasswordDTO.getPhone(), resetPayPasswordDTO.getEmail());

        /* 2.设置支付密码 */
        setPayPassword(user, resetPayPasswordDTO.getVerifyType(), resetPayPasswordDTO.getCode(), resetPayPasswordDTO.getPayPassword());
    }

    /**
     * 设置支付密码
     *
     * @param user        用户信息
     * @param verifyType  验证方式
     * @param code        验证码
     * @param payPassword 支付密码
     */
    private void setPayPassword(User user, Integer verifyType, String code, String payPassword) {

        /* 1.检查验证码 */
        codeService.verifyCode(VerifyCaseEnum.PAY_PASSWORD.getCode(), getSendTo(user, verifyType), code);

        /* 2.修改登录密码 */
        user.setPayPassword(StringUtil.toMd5(payPassword));
        user.setModifiedBy(null);
        user.setModifiedTime(DateUtil.now());
        userRepository.save(user);
    }

    /**
     * 绑定手机号
     *
     * @param user        用户
     * @param setPhoneDTO 绑定手机号信息
     */
    @Override
    public void setPhone(User user, SetPhoneDTO setPhoneDTO) {

        /* 1.检查手机号是否存在 */
        existsPhone(setPhoneDTO.getAreaCode(), setPhoneDTO.getPhone());

        /* 2.检查验证码 */
        codeService.verifyCode(VerifyCaseEnum.PHONE.getCode(), setPhoneDTO.getAreaCode().concat(setPhoneDTO.getPhone()), setPhoneDTO.getCode());

        /* 3.设置手机号 */
        user.setAreaCode(setPhoneDTO.getAreaCode());
        user.setPhone(setPhoneDTO.getPhone());
        user.setModifiedBy(null);
        user.setModifiedTime(DateUtil.now());
        userRepository.save(user);
    }

    /**
     * 绑定邮箱
     *
     * @param user        用户
     * @param setEmailDTO 绑定邮箱信息
     */
    @Override
    public void setEmail(User user, SetEmailDTO setEmailDTO) {

        /* 1.检查邮箱是否存在 */
        existsEmail(setEmailDTO.getEmail());

        /* 2.检查验证码 */
        codeService.verifyCode(VerifyCaseEnum.EMAIL.getCode(), setEmailDTO.getEmail(), setEmailDTO.getCode());

        /* 3.设置手机号 */
        user.setEmail(setEmailDTO.getEmail());
        user.setModifiedBy(null);
        user.setModifiedTime(DateUtil.now());
        userRepository.save(user);
    }

    /**
     * 检查用户名是否存在
     *
     * @param userName 用户名
     */
    @Override
    public void existsUserName(String userName) {
        boolean existsByUserName = userRepository.existsByUserName(userName);
        if (existsByUserName) {
            throw new BusinessException(ErrorEnum.USER_EXISTED);
        }
    }

    /**
     * 检查手机号是否存在
     *
     * @param areaCode 手机号归属地代码
     * @param phone    手机号
     */
    @Override
    public void existsPhone(String areaCode, String phone) {
        boolean existsByAreaCodeAndPhone = userRepository.existsByAreaCodeAndPhone(areaCode, phone);
        if (existsByAreaCodeAndPhone) {
            throw new BusinessException(ErrorEnum.USER_EXISTED);
        }
    }

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     */
    @Override
    public void existsEmail(String email) {
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            throw new BusinessException(ErrorEnum.USER_EXISTED);
        }
    }

    /**
     * 设置是否订阅邮件
     *
     * @param user             用户
     * @param subscribeMailDTO 订阅邮件信息
     */
    @Override
    public void subscribeMail(User user, SubscribeMailDTO subscribeMailDTO) {
        user.setIsSubscribeMail(subscribeMailDTO.getIsSubscribeMail());
        user.setModifiedBy(null);
        user.setModifiedTime(DateUtil.now());
        userRepository.save(user);
    }

    /**
     * 修改用户信息
     *
     * @param admin   管理员
     * @param userId  被修改的用户id
     * @param userDTO 用户信息
     */
    @Override
    public void modifyUser(ManagerAdmin admin, Long userId, UserDTO userDTO) {

        /* 1.取得被修改的内容 */
        User user = userRepository.findNotNullById(userId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(userDTO.getModifiedTime(), user.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        user.setRemark(userDTO.getRemark());
        user.setStatus(userDTO.getStatus());
        user.setModifiedBy(admin);
        user.setModifiedTime(DateUtil.now());
        userRepository.save(user);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_USER, userId);
    }

    /**
     * 取得邀请信息
     *
     * @param user 用户
     * @return 邀请信息
     */
    @Override
    public InviteVO findInviteInfo(User user) {

        /* 1.查询邀请信息 */
        /* 1.1.计算当前用户邀请次数 */
        Integer invitationTimes = userRepository.countByParentId(user.getId());

        /* 1.2.计算当前用户剩余奖励次数（奖励次数 - 已邀请次数） */
        Integer rewardTimes = inviteRewardTimes - invitationTimes;
        if (rewardTimes < 0) {
            rewardTimes = 0;
        }

        /* 1.3.app下载页面 */
        String urlAppDownloadParamKey = LanguageUtil.getTextByLanguage(SettingsConstant.URL_APP_DOWNLOAD.concat(SettingsConstant.ZH), SettingsConstant.URL_APP_DOWNLOAD.concat(SettingsConstant.EN));
        SystemSettings urlAppDownload = systemSettingsService.findSystemSettings(urlAppDownloadParamKey);

        /* 1.4.背景图地址 */
        String urlInvitePidParamKey = LanguageUtil.getTextByLanguage(SettingsConstant.URL_INVITE_PIC.concat(SettingsConstant.ZH), SettingsConstant.URL_INVITE_PIC.concat(SettingsConstant.EN));
        SystemSettings systemSettings = systemSettingsService.findSystemSettings(urlInvitePidParamKey);

        /* 2.返回邀请信息 */
        InviteVO inviteVO = new InviteVO();

        inviteVO.setInviteCode(user.getInviteCode());
        inviteVO.setInviteTimes(invitationTimes);
        inviteVO.setRewardTimes(rewardTimes);
        inviteVO.setDownloadUrl(urlAppDownload.getParamValue());
        inviteVO.setBackgroundUrl(systemSettings.getParamValue());

        return inviteVO;
    }

    /**
     * 导出用户邮箱列表
     *
     * @return 导出数据
     */
    @Override
    public ResponseEntity downloadEmail() {

        String fileName = "email.csv";

        List<User> userList = userRepository.findByIsSubscribeMailIsTrue();
        if (ObjectUtil.isEmptyCollection(userList)) {
            throw new DataNotFoundException();
        }

        String[] title = {"id", "用户名", "邮箱地址"};
        List<String[]> dataList = new ArrayList<>();

        String[] data;
        for (User user : userList) {
            data = new String[3];
            data[0] = String.valueOf(user.getId());
            data[1] = user.getUserName();
            data[2] = user.getEmail();
            dataList.add(data);
        }

        ResponseEntity responseEntity;

        try {
            responseEntity = csvHelper.download(fileName, title, dataList);
        } catch (IOException e) {
            throw new ActionException();
        }

        return responseEntity;
    }

    /**
     * 根据邀请码查询用户信息
     *
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    @Override
    public UserVO findUserByInviteCode(String inviteCode) {

        /* 1.取得用户信息 */
        User user = findByInviteCode(inviteCode);
        if (ObjectUtil.isEmpty(user)) {
            throw new DataNotFoundException();
        }

        /* 2.设置项目信息 */
        return editUserVO(user);
    }

    /**
     * 取得验证码发送地址
     *
     * @param user       用户信息
     * @param verifyType 验证方式
     * @return 验证码发送地址
     */
    private String getSendTo(User user, Integer verifyType) {
        // 手机短信验证的场合，返回手机号码
        if (ObjectUtil.equals(verifyType, VerifyTypeEnum.PHONE.getCode())) {
            return user.getAreaCode().concat(user.getPhone());
        }
        // 邮件验证的场合，返回邮箱
        else {
            return user.getEmail();
        }
    }

    /**
     * 编辑用户VO
     *
     * @param user 用户信息
     * @return 用户VO
     */
    private UserVO editUserVO(User user) {

        UserVO userVO = new UserVO();

        userVO.setId(user.getId());
        userVO.setUserName(user.getUserName());
        userVO.setHasPayPassword(ObjectUtil.isNotEmpty(user.getPayPassword()) ? true : false);
        userVO.setAreaCode(user.getAreaCode());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setIsSubscribeMail(user.getIsSubscribeMail());
        userVO.setInviteCode(user.getInviteCode());
        userVO.setParentId(user.getParentId());
        userVO.setRemark(user.getRemark());
        userVO.setStatus(user.getStatus());
        userVO.setStatusName(StatusEnum.getNameByCode(user.getStatus()));
        userVO.setCreatedBy(ObjectUtil.isNotEmpty(user.getCreatedBy()) ? user.getCreatedBy().getName() : null);
        userVO.setCreatedTime(user.getCreatedTime());
        userVO.setModifiedBy(ObjectUtil.isNotEmpty(user.getModifiedBy()) ? user.getModifiedBy().getName() : null);
        userVO.setModifiedTime(user.getModifiedTime());

        return userVO;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    private User findUserById(Long userId) {
        return userRepository.findNotNullById(userId);
    }


    /**
     * 根据验证信息查询用户信息
     *
     * @param verifyType 验证方式
     * @param areaCode   手机号归属地代码
     * @param phone      手机号
     * @param email      邮箱
     * @return 用户信息
     */
    private User findUserByVerifyInfo(Integer verifyType, String areaCode, String phone, String email) {

        User user;

        if (ObjectUtil.equals(verifyType, VerifyTypeEnum.PHONE.getCode())) {
            user = userRepository.findByAreaCodeAndPhone(areaCode, phone);
        } else {
            user = userRepository.findByEmail(email);
        }

        if (ObjectUtil.isEmpty(user)) {
            throw new DataNotFoundException();
        }

        return user;
    }

    /**
     * 根据登录账号查询用户信息
     *
     * @param account 登录账号
     * @return 用户信息
     */
    private User findUserByAccount(String account) {

        /* 1.用户名方式登录 */
        User user = userRepository.findByUserName(account);
        if (ObjectUtil.isNotEmpty(user)) {
            return user;
        }

        /* 2.手机号方式登录 */
        // 登录时可能输入【手机号】或者【归属地代码+手机号】两种格式
        List<User> userList = userRepository.findByPhone(account);
        if (ObjectUtil.isNotEmptyCollection(userList)) {
            // 未输入归属地代码的场合，手机号可能相同
            if (userList.size() > 1) {
                // 提示请输入归属地代码
                throw new BusinessException(ErrorEnum.AREA_CODE_NOT_SET);
            } else {
                return userList.get(0);
            }
        }

        /* 3.邮箱方式登录 */
        user = userRepository.findByEmail(account);

        return user;
    }

    /**
     * 根据邀请码查询用户信息
     *
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    @Override
    public User findByInviteCode(String inviteCode) {
        User user = userRepository.findByInviteCode(inviteCode);
        if (ObjectUtil.isEmpty(user)) {
            throw new BusinessException(ErrorEnum.INVITATION_CODE_ERROR);
        }
        return user;
    }
}