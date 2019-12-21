package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;

/**
 * 管理日志Service
 *
 * @author sunli
 * @date 2018/12/07
 */
public interface ManagerLogService {

    /**
     * 保存日志
     *
     * @param admin  管理员
     * @param remark 操作备注
     * @param id     操作记录id
     */
    void saveManagerLog(ManagerAdmin admin, String remark, Long id);

    /**
     * 保存日志（未取得管理员id的场合，记录登录时用户名）
     *
     * @param adminName 管理员用户名
     * @param remark    操作备注
     */
    void saveManagerLog(String adminName, String remark);
}