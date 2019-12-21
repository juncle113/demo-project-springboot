package com.cpto.dapp.constant;

import java.math.BigDecimal;

/**
 * 全局常量类
 *
 * @author sunli
 * @date 2018/12/07
 */
public class Constant extends com.cpto.dapp.common.constant.Constant {

    /**
     * 币种区分：USDT
     */
    public static final String COIN_USDT = "USDT";

    /**
     * 币种区分：CPTO
     */
    public static final String COIN_CPTO = "CPTO";

    /**
     * 币种区分：BTC
     */
    public static final String COIN_BTC = "BTC";

    /**
     * 币种区分：ETH
     */
    public static final String COIN_ETH = "ETH";

    /**
     * 排序项目：id
     */
    public static final String SORT_KEY_ID = "id";

    /**
     * ETH计算用转换精度
     */
    public static final BigDecimal ETH_CONVERT_DEGREE = new BigDecimal("1000000000000000000");
}