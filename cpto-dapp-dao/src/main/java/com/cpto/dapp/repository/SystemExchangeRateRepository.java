package com.cpto.dapp.repository;


import com.cpto.dapp.domain.SystemExchangeRate;

/**
 * 币种汇率Repository
 *
 * @author sunli
 * @date 2019/03/05
 */
public interface SystemExchangeRateRepository extends BaseRepository<SystemExchangeRate, Long> {

    /**
     * 查询对应币种的汇率信息
     *
     * @param fromCoinKind 源币种
     * @param toCoinKind   目标币种
     * @return 汇率信息
     */
    SystemExchangeRate findByFromCoinKindAndToCoinKind(String fromCoinKind, String toCoinKind);
}