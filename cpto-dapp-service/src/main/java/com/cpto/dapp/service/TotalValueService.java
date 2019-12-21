package com.cpto.dapp.service;

import com.cpto.dapp.pojo.vo.TotalValueVO;

/**
 * 价值总量Service
 *
 * @author sunli
 * @date 2019/01/09
 */
public interface TotalValueService extends BaseService {

    /**
     * 取得价值总量信息
     *
     * @return 价值总量
     */
    TotalValueVO findTotalValue();
}