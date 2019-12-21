package com.cpto.dapp.common.util;


import java.math.BigDecimal;

/**
 * 数学工具类
 *
 * @author sunli
 * @date 2019/01/19
 */
public class MathUtil {

    /**
     * BigDecimal除法计算，结果向上舍入小数点后指定位数
     *
     * @param scale  精度位数
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal divideRoundUp(int scale, BigDecimal... params) {
        return divide(scale, BigDecimal.ROUND_UP, params);
    }

    /**
     * BigDecimal除法计算，结果向上舍入小数点后第6位
     *
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal divideRoundUp(BigDecimal... params) {
        return divideRoundUp(12, params);
    }

    /**
     * BigDecimal除法计算，结果向下舍入小数点后指定位数
     *
     * @param scale  精度位数
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal divideRoundDown(int scale, BigDecimal... params) {
        return divide(scale, BigDecimal.ROUND_DOWN, params);
    }

    /**
     * BigDecimal除法计算，结果向下舍入小数点后第6位
     *
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal divideRoundDown(BigDecimal... params) {
        return divideRoundDown(12, params);
    }

    /**
     * BigDecimal乘法计算，结果向上舍入小数点后指定位数
     *
     * @param scale  精度位数
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal multiplyRoundUp(int scale, BigDecimal... params) {
        return multiply(scale, BigDecimal.ROUND_UP, params);
    }

    /**
     * BigDecimal乘法计算，结果向上舍入小数点后第6位
     *
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal multiplyRoundUp(BigDecimal... params) {
        return multiplyRoundUp(12, params);
    }

    /**
     * BigDecimal乘法计算，结果向下舍入小数点后指定位数
     *
     * @param scale  精度位数
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal multiplyRoundDown(int scale, BigDecimal... params) {
        return multiply(scale, BigDecimal.ROUND_DOWN, params);
    }

    /**
     * BigDecimal乘法计算，结果向下舍入小数点后第6位
     *
     * @param params 计算参数
     * @return 计算结果
     */
    public static BigDecimal multiplyRoundDown(BigDecimal... params) {
        return multiplyRoundDown(12, params);
    }

    /**
     * BigDecimal乘法计算
     *
     * @param scale  精度位数
     * @param round  舍入模式
     * @param params 计算参数
     * @return 计算结果
     */
    private static BigDecimal multiply(int scale, int round, BigDecimal... params) {

        checkParams(params);

        BigDecimal result = BigDecimal.ONE;

        for (BigDecimal param : params) {
            result = result.multiply(param).setScale(scale, round);
        }

        return result;
    }

    /**
     * BigDecimal除法计算
     *
     * @param scale  精度位数
     * @param round  舍入模式
     * @param params 计算参数
     * @return 计算结果
     */
    private static BigDecimal divide(int scale, int round, BigDecimal... params) {

        checkParams(params);

        BigDecimal result = params[0];

        for (int i = 1; i < params.length; i++) {
            result = result.divide(ObjectUtil.nullToObject(params[i]), scale, round);
        }

        return result;
    }


    /**
     * 计算参数检查
     *
     * @param params 计算参数
     * @throws IllegalArgumentException 参数异常
     */
    private static void checkParams(Object... params) {
        if (ObjectUtil.isEmpty(params) || params.length < 2) {
            throw new IllegalArgumentException();
        }
    }
}