package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.AdminDTO;
import com.cpto.dapp.pojo.dto.AdminLoginDTO;
import com.cpto.dapp.pojo.vo.AdminLoginVO;
import com.cpto.dapp.pojo.vo.AdminVO;

import java.util.List;

/**
 * 管理员Service
 *
 * @author sunli
 * @date 2018/12/07
 */
public interface ManagerAdminService extends BaseService {

    /**
     * 登录处理
     *
     * @param adminLoginDTO 管理员登录信息
     * @return 管理员登录信息
     */
    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

    /**
     * 注销处理
     *
     * @param admin 管理员
     */
    void logout(ManagerAdmin admin);

    /**
     * 根据id取得管理员信息
     *
     * @param adminId 管理员id
     * @return 管理员信息
     */
    AdminVO findAdmin(Long adminId);

    /**
     * 根据id取得管理员信息（记录其他业务的管理信息）
     *
     * @param adminId 管理员id
     * @return 管理员信息
     */
    ManagerAdmin getAdmin(Long adminId);

    /**
     * 取得全部管理员信息
     *
     * @return 管理员信息列表
     */
    List<AdminVO> findAdminList();

    /**
     * 新增管理员
     *
     * @param admin    管理员
     * @param adminDTO 新增的管理员信息
     */
    void addAdmin(ManagerAdmin admin, AdminDTO adminDTO);

    /**
     * 修改管理员
     *
     * @param admin    管理员
     * @param adminId  被修改的管理员id
     * @param adminDTO 被修改的管理员信息
     */
    void modifyAdmin(ManagerAdmin admin, Long adminId, AdminDTO adminDTO);

    /**
     * 删除管理员
     *
     * @param admin   管理员
     * @param adminId 被删除的管理员id
     */
    void removeAdmin(ManagerAdmin admin, Long adminId);
}