package com.cpto.dapp.service;

import com.cpto.dapp.pojo.vo.SystemNoticeVO;
import com.cpto.dapp.pojo.vo.VersionVO;

import java.util.List;

/**
 * 系统信息Service
 *
 * @author sunli
 * @date 2019/01/10
 */
public interface SystemInfoService extends BaseService {

    /**
     * 检查版本
     *
     * @param deviceType     设备类型
     * @param currentVersion 客户端当前版本号
     * @return 最新版本信息
     */
    VersionVO checkVersion(Integer deviceType, String currentVersion);

    /**
     * 取得系统公告
     *
     * @return 系统公告
     */
    List<SystemNoticeVO> findSystemNoticeList();
}