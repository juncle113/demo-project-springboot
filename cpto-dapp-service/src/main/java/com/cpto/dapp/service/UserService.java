package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.*;
import com.cpto.dapp.pojo.vo.InviteVO;
import com.cpto.dapp.pojo.vo.LoginVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.UserVO;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

/**
 * 用户Service
 *
 * @author sunli
 * @date 2018/12/29
 */
public interface UserService extends BaseService {

    /**
     * 注册新的用户
     *
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO);

    /**
     * 登录处理
     *
     * @param loginDTO 用户登录信息
     * @return 用户登录信息
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 注销处理
     *
     * @param user 用户
     */
    void logout(User user);

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
    PageVO<UserVO> searchUser(Timestamp searchTime,
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
                              String toCreatedTime);

    /**
     * 根据id取得用户
     *
     * @param userId 用户id
     * @return 用户
     */
    UserVO findUser(Long userId);

    /**
     * 根据id取得用户
     *
     * @param user 用户
     * @return 用户
     */
    UserVO findUser(User user);

    /**
     * 根据id取得用户
     *
     * @param userId 用户id
     * @return 用户
     */
    User getUser(Long userId);

    /**
     * 修改登录密码
     *
     * @param user              用户
     * @param modifyPasswordDTO 修改登录密码信息
     */
    void modifyPassword(User user, ModifyPasswordDTO modifyPasswordDTO);

    /**
     * 重置登录密码
     *
     * @param resetPasswordDTO 重置登录密码信息
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * 修改支付密码
     *
     * @param user                 用户
     * @param modifyPayPasswordDTO 修改支付密码信息
     */
    void modifyPayPassword(User user, ModifyPayPasswordDTO modifyPayPasswordDTO);

    /**
     * 重置支付密码
     *
     * @param resetPayPasswordDTO 重置支付密码信息
     */
    void resetPayPassword(ResetPayPasswordDTO resetPayPasswordDTO);

    /**
     * 绑定手机号
     *
     * @param user        用户
     * @param setPhoneDTO 绑定手机号信息
     */
    void setPhone(User user, SetPhoneDTO setPhoneDTO);

    /**
     * 绑定邮箱
     *
     * @param user        用户
     * @param setEmailDTO 绑定邮箱信息
     */
    void setEmail(User user, SetEmailDTO setEmailDTO);

    /**
     * 检查用户名是否存在
     *
     * @param userName 用户名
     */
    void existsUserName(String userName);

    /**
     * 检查手机号是否存在
     *
     * @param areaCode 手机号归属地代码
     * @param phone    手机号
     */
    void existsPhone(String areaCode, String phone);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     */
    void existsEmail(String email);

    /**
     * 设置是否订阅邮件
     *
     * @param user             用户
     * @param subscribeMailDTO 订阅邮件信息
     */
    void subscribeMail(User user, SubscribeMailDTO subscribeMailDTO);

    /**
     * 修改用户信息
     *
     * @param admin   管理员
     * @param userId  被修改的用户id
     * @param userDTO 用户信息
     */
    void modifyUser(ManagerAdmin admin, Long userId, UserDTO userDTO);

    /**
     * 取得邀请信息
     *
     * @param user 用户
     * @return 邀请信息
     */
    InviteVO findInviteInfo(User user);

    /**
     * 导出用户邮箱列表
     *
     * @return 导出数据
     */
    ResponseEntity downloadEmail();

    /**
     * 根据邀请码查询用户信息
     *
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    UserVO findUserByInviteCode(String inviteCode);

    /**
     * 根据邀请码查询用户信息
     *
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    User findByInviteCode(String inviteCode);
}