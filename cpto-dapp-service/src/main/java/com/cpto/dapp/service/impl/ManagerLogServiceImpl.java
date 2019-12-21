package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.StringUtil;
import com.cpto.dapp.domain.LogManager;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.repository.ManagerLogRepository;
import com.cpto.dapp.service.ManagerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理日志ServiceImpl
 *
 * @author sunli
 * @date 2018/12/07
 */
@Service
public class ManagerLogServiceImpl extends BaseServiceImpl implements ManagerLogService {

    @Autowired
    private ManagerLogRepository managerLogRepository;

    /**
     * 保存日志
     *
     * @param admin  管理员
     * @param remark 操作备注
     */
    @Override
    public void saveManagerLog(ManagerAdmin admin, String remark, Long id) {

        // 根据管理员id查询管理员用户名
        LogManager logManager = new LogManager();
        logManager.setRemark(editLogRemark(admin.getName(),
                StringUtil.nullToBlank(remark).concat("[").concat(String.valueOf(id)).concat("]")));
        logManager.setStatus(StatusEnum.VALID.getCode());
        logManager.setCreatedBy(admin);
        logManager.setCreatedTime(DateUtil.now());
        managerLogRepository.save(logManager);
    }

    /**
     * 保存日志（未取得管理员id的场合，记录登录时用户名）
     *
     * @param adminName 管理员用户名
     * @param remark    操作备注
     */
    @Override
    public void saveManagerLog(String adminName, String remark) {
        LogManager logManager = new LogManager();
        logManager.setStatus(StatusEnum.VALID.getCode());
        logManager.setRemark(editLogRemark(adminName, remark));
        logManager.setCreatedTime(DateUtil.now());
        managerLogRepository.save(logManager);
    }

    /**
     * 编辑日志备注
     *
     * @param adminUserName 管理员用户名
     * @param message       备注信息
     * @return 备注
     */
    private String editLogRemark(String adminUserName, String message) {
        return adminUserName.concat(StringUtil.HALF_SPACE).concat(message);
    }
}
