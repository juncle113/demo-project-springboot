package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.SystemExchangeRate;
import com.cpto.dapp.pojo.dto.ExchangeRateDTO;
import com.cpto.dapp.pojo.vo.ExchangeRateVO;

import java.util.List;

/**
 * 币种汇率Service
 *
 * @author sunli
 * @date 2019/03/05
 */
public interface ExchangeRateService extends BaseService {

    /**
     * 根据币种查询汇率信息
     *
     * @param fromCoinKind 源币种
     * @param toCoinKind   目标币种
     * @return 币种汇率信息
     */
    ExchangeRateVO findExchangeVORateByCoin(String fromCoinKind, String toCoinKind);

    /**
     * 根据币种查询汇率信息
     *
     * @param fromCoinKind 源币种
     * @param toCoinKind   目标币种
     * @return 币种汇率信息
     */
    SystemExchangeRate findExchangeRateByCoin(String fromCoinKind, String toCoinKind);

    /**
     * 查询汇率信息列表
     *
     * @return 币种汇率信息列表
     */
    List<ExchangeRateVO> findExchangeRateList();

    /**
     * 根据币种汇率id查询汇率信息
     *
     * @param exchangeRateId 币种汇率id
     * @return 币种汇率信息
     */
    ExchangeRateVO findExchangeRate(Long exchangeRateId);

    /**
     * 修改币种汇率
     *
     * @param admin           管理员
     * @param exchangeRateId  币种汇率id
     * @param exchangeRateDTO 币种汇率信息
     */
    void modifyExchangeRate(ManagerAdmin admin, Long exchangeRateId, ExchangeRateDTO exchangeRateDTO);
}