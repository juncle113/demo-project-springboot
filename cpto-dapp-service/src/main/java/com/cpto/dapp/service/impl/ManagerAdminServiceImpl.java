package com.cpto.dapp.service.impl;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.IdUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.common.util.StringUtil;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.enums.AdminRoleEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.*;
import com.cpto.dapp.pojo.dto.AdminDTO;
import com.cpto.dapp.pojo.dto.AdminLoginDTO;
import com.cpto.dapp.pojo.vo.AdminLoginVO;
import com.cpto.dapp.pojo.vo.AdminVO;
import com.cpto.dapp.repository.ManagerAdminRepository;
import com.cpto.dapp.service.ManagerAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员ServiceImpl
 *
 * @author sunli
 * @date 2018/12/07
 */
@Service
public class ManagerAdminServiceImpl extends BaseServiceImpl implements ManagerAdminService {

    @Autowired
    private ManagerAdminRepository managerAdminRepository;

    @Autowired
    private AuthManager authManager;

    /**
     * 登录处理
     *
     * @param adminLoginDTO 管理员登录信息
     * @return 管理员登录信息
     */
    @Override
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO) {

        /* 1.根据管理员账号查询管理员信息 */
        ManagerAdmin admin = findAdminByName(adminLoginDTO.getUserName());

        /* 2.通过MD5加密登录密码 */
        String md5Password = StringUtil.toMd5(adminLoginDTO.getPassword());

        /* 3.进行登录检查 */
        // 登录失败：账号或密码错误的场合
        if (ObjectUtil.isEmpty(admin) || ObjectUtil.notEquals(md5Password, admin.getPassword())) {
            managerLogService.saveManagerLog(adminLoginDTO.getUserName(), ManagerLogConstant.LOGIN_FAILED);
            throw new LoginException();
        }

        // 登录失败：账号被禁用的场合
        if (ObjectUtil.equals(admin.getStatus(), StatusEnum.INVALID.getCode())) {
            managerLogService.saveManagerLog(admin, ManagerLogConstant.LOGIN_FORBIDDEN, admin.getId());
            throw new AuthorizedException();
        }

        /* 4.创建token，并缓存 */
        String token = authManager.createToken(admin.getId());

        /* 5.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.LOGIN_SUCCESS, admin.getId());

        /* 6.登录成功，返回登录信息 */
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        adminLoginVO.setToken(token);

        return adminLoginVO;
    }

    /**
     * 注销处理
     *
     * @param admin 管理员
     */
    @Override
    public void logout(ManagerAdmin admin) {
        // 清除缓存中的token
        authManager.removeToken(admin.getId());
    }

    /**
     * 根据id取得管理员信息
     *
     * @param adminId 管理员id
     * @return 管理员信息
     */
    @Override
    public AdminVO findAdmin(Long adminId) {
        ManagerAdmin managerAdmin = findAdminById(adminId);
        return editAdminVO(managerAdmin);
    }

    /**
     * 根据id取得管理员信息（关联其他业务用）
     *
     * @param adminId 管理员id
     * @return 管理员信息
     */
    @Override
    public ManagerAdmin getAdmin(Long adminId) {
        return findAdminById(adminId);
    }

    /**
     * 取得全部管理员信息
     *
     * @return 管理员信息列表
     */
    @Override
    public List<AdminVO> findAdminList() {

        List<ManagerAdmin> managerAdminList = managerAdminRepository.findAll();

        List<AdminVO> adminVOs = new ArrayList<>();
        for (ManagerAdmin managerAdmin : managerAdminList) {
            adminVOs.add(editAdminVO(managerAdmin));
        }

        return adminVOs;
    }

    /**
     * 新增管理员
     *
     * @param admin    管理员
     * @param adminDTO 新增的管理员信息
     */
    @Override
    public void addAdmin(ManagerAdmin admin, AdminDTO adminDTO) {

        /* 1.检查管理员账号是否存在 */
        // 不包括已被逻辑删除的账号
        boolean existsByUserName = managerAdminRepository.existsByUserName(adminDTO.getUserName());
        if (existsByUserName) {
            throw new DataExistedException();
        }

        /* 2.设置新增账号信息 */
        ManagerAdmin managerAdmin = new ManagerAdmin();
        managerAdmin.setId(IdUtil.generateIdByCurrentTime());
        managerAdmin.setUserName(adminDTO.getUserName());
        managerAdmin.setName(adminDTO.getName());
        managerAdmin.setPassword(StringUtil.toMd5(adminDTO.getPassword()));
        managerAdmin.setRoleType(adminDTO.getRoleType());
        managerAdmin.setRemark(adminDTO.getRemark());
        managerAdmin.setStatus(adminDTO.getStatus());
        managerAdmin.setDeleted(false);
        managerAdmin.setCreatedTime(DateUtil.now());
        managerAdmin.setCreatedBy(admin);
        managerAdmin = managerAdminRepository.save(managerAdmin);

        /* 3.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.ADD_ADMIN, managerAdmin.getId());
    }

    /**
     * 修改管理员
     *
     * @param admin    管理员
     * @param adminId  被修改的管理员id
     * @param adminDTO 被修改的管理员信息
     */
    @Override
    public void modifyAdmin(ManagerAdmin admin, Long adminId, AdminDTO adminDTO) {

        /* 1.取得被修改的内容 */
        ManagerAdmin managerAdmin = findAdminById(adminId);

        /* 2.检查被修改账号角色（无权处理root） */
        checkoutAdminRole(managerAdmin.getRoleType());

        /* 3.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(adminDTO.getModifiedTime(), managerAdmin.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 4.设置修改内容 */
        managerAdmin.setName(adminDTO.getName());
        managerAdmin.setPassword(StringUtil.toMd5(adminDTO.getPassword()));
        managerAdmin.setRoleType(adminDTO.getRoleType());
        managerAdmin.setRemark(adminDTO.getRemark());
        managerAdmin.setStatus(adminDTO.getStatus());
        managerAdmin.setModifiedTime(DateUtil.now());
        managerAdmin.setModifiedBy(admin);
        managerAdminRepository.save(managerAdmin);

        /* 5.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_ADMIN, managerAdmin.getId());
    }

    /**
     * 删除管理员
     *
     * @param admin   管理员
     * @param adminId 被删除的管理员id
     */
    @Override
    public void removeAdmin(ManagerAdmin admin, Long adminId) {

        /* 1.检查账号角色（无权处理root） */
        ManagerAdmin managerAdmin = findAdminById(adminId);
        checkoutAdminRole(managerAdmin.getRoleType());

        /* 2.获取删除信息 */
        ManagerAdmin existsById = managerAdminRepository.findNotNullById(adminId);

        /* 3.删除账号 */
        managerAdmin.setDeleted(true);
        managerAdmin.setStatus(StatusEnum.INVALID.getCode());
        managerAdmin.setModifiedBy(admin);
        managerAdmin.setModifiedTime(DateUtil.now());
        managerAdminRepository.save(managerAdmin);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.REMOVE_ADMIN, managerAdmin.getId());
    }

    /**
     * 编辑管理员VO
     *
     * @param managerAdmin 管理员信息
     * @return 管理员VO
     */
    private AdminVO editAdminVO(ManagerAdmin managerAdmin) {

        AdminVO adminVO = new AdminVO();
        adminVO.setId(managerAdmin.getId());
        adminVO.setUserName(managerAdmin.getUserName());
        adminVO.setName(managerAdmin.getName());
        adminVO.setRoleType(managerAdmin.getRoleType());
        adminVO.setRoleTypeName(AdminRoleEnum.getNameByCode(managerAdmin.getRoleType()));
        adminVO.setRemark(managerAdmin.getRemark());
        adminVO.setStatus(managerAdmin.getStatus());
        adminVO.setStatusName(StatusEnum.getNameByCode(managerAdmin.getStatus()));
        adminVO.setCreatedTime(managerAdmin.getCreatedTime());
        adminVO.setCreatedBy(ObjectUtil.isNotEmpty(managerAdmin.getCreatedBy()) ? managerAdmin.getCreatedBy().getName() : null);
        adminVO.setModifiedTime(managerAdmin.getModifiedTime());
        adminVO.setModifiedBy(ObjectUtil.isNotEmpty(managerAdmin.getCreatedBy()) ? managerAdmin.getCreatedBy().getName() : null);

        return adminVO;
    }

    /**
     * 检查账号角色
     * 不能处理root账号
     *
     * @param roleType 账号角色
     * @throws AuthorizedException 权限异常
     */
    private void checkoutAdminRole(Integer roleType) {

        if (ObjectUtil.isEmpty(roleType)) {
            throw new ParameterException();
        }

        // 处理root账号的场合，提示无权限
        if (ObjectUtil.equals(roleType, AdminRoleEnum.ROOT.getCode())) {
            throw new AuthorizedException();
        }
    }

    /**
     * 根据管理员id查询管理员信息
     *
     * @param adminId 管理员id
     * @return 管理员信息
     */
    private ManagerAdmin findAdminById(Long adminId) {
        return managerAdminRepository.findNotNullById(adminId);
    }

    /**
     * 根据管理员账号查询管理员信息
     *
     * @param userName 账号
     * @return 管理员信息
     */
    private ManagerAdmin findAdminByName(String userName) {
        ManagerAdmin managerAdmin = managerAdminRepository.findByUserName(userName);
        if (ObjectUtil.isEmpty(managerAdmin)) {
            throw new DataNotFoundException();
        }
        return managerAdmin;
    }
}