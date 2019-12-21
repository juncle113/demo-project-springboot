package com.cpto.dapp.repository;


import com.cpto.dapp.domain.SystemTotalValue;

import java.util.List;

/**
 * 价值设置Repository
 *
 * @author sunli
 * @date 2019/01/10
 */
public interface SystemTotalValueRepository extends BaseRepository<SystemTotalValue, Long> {

    /**
     * 根据设置类型查询价值设置信息
     *
     * @param valueType 价值类型
     * @return 价值设置信息
     */
    List<SystemTotalValue> findByValueTypeOrderBySortNo(Integer valueType);
}