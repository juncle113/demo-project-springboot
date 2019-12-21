package com.cpto.dapp.common.util;

import java.math.BigDecimal;

/**
 * 数字货币工具类
 *
 * @author sunli
 * @date 2019/04/22
 */
public class CoinUtil {

    /**
     * btc兑换cpto
     *
     * @param amount         兑换数额
     * @param btcToUsdtRate  btc兑换usdt汇率
     * @param cptoToUsdtRate cpto兑换usdt汇率
     * @return 兑换结果
     */
    public static BigDecimal btcToCpto(BigDecimal amount, BigDecimal btcToUsdtRate, BigDecimal cptoToUsdtRate) {

        BigDecimal usdtAmount = MathUtil.multiplyRoundDown(amount, btcToUsdtRate);
        BigDecimal cptoAmount = MathUtil.divideRoundDown(usdtAmount, cptoToUsdtRate);

        return cptoAmount;
    }


    /**
     * eth兑换cpto
     *
     * @param amount         兑换数额
     * @param ethToUsdtRate  eth兑换usdt汇率
     * @param cptoToUsdtRate cpto兑换usdt汇率
     * @return 兑换结果
     */
    public static BigDecimal ethToCpto(BigDecimal amount, BigDecimal ethToUsdtRate, BigDecimal cptoToUsdtRate) {

        BigDecimal usdtAmount = MathUtil.multiplyRoundDown(amount, ethToUsdtRate);
        BigDecimal cptoAmount = MathUtil.divideRoundDown(usdtAmount, cptoToUsdtRate);

        return cptoAmount;
    }

    /**
     * cpto兑换btc汇率
     *
     * @param cptoToUsdtRate cpto兑换usdt汇率
     * @param btcToUsdtRate  btc兑换usdt汇率
     * @return 兑换结果
     */
    public static BigDecimal cptoToBtcRate(BigDecimal cptoToUsdtRate, BigDecimal btcToUsdtRate) {
        return MathUtil.divideRoundDown(12, cptoToUsdtRate, btcToUsdtRate);
    }

    /**
     * cpto兑换eth汇率
     *
     * @param cptoToUsdtRate cpto兑换usdt汇率
     * @param ethToUsdtRate  eth兑换usdt汇率
     * @return 兑换结果
     */
    public static BigDecimal cptoToEthRate(BigDecimal cptoToUsdtRate, BigDecimal ethToUsdtRate) {
        return MathUtil.divideRoundDown(12, cptoToUsdtRate, ethToUsdtRate);
    }
}