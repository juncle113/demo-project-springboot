package com.cpto.dapp.repository;


import com.cpto.dapp.domain.SystemVersion;

import java.util.List;

/**
 * 版本Repository
 *
 * @author sunli
 * @date 2019/01/08
 */
public interface SystemVersionRepository extends BaseRepository<SystemVersion, Long> {

    /**
     * 取得最新版本信息
     *
     * @param deviceType 设备类型
     * @param statusList 状态
     * @return 最新版本信息
     */
    SystemVersion findTopByDeviceTypeAndStatusInOrderByIdDesc(Integer deviceType, List<Integer> statusList);
}