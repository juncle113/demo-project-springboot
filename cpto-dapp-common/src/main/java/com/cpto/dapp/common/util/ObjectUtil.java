package com.cpto.dapp.common.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 基础对象工具类
 *
 * @author sunli
 * @date 2019/01/02
 */
public class ObjectUtil {

    /**
     * 检查是否为空对象或空字符串
     *
     * @param object 需要判断的对象
     * @return 检查结果
     */
    public static boolean isEmpty(Object object) {

        boolean result = false;

        if (object instanceof String) {
            if (StringUtils.isEmpty(object)
                    || StringUtils.isEmpty(((String) object).trim())
                    || ObjectUtil.equals(((String) object).toLowerCase(), StringUtil.NULL)) {
                result = true;
            }
        } else {
            if (Objects.isNull(object)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * 检查是否不为空对象或空字符串
     *
     * @param object 需要判断的对象
     * @return 检查结果
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 检查是否为空集合
     *
     * @param collection 需要判断的集合
     * @return 检查结果
     */
    public static boolean isEmptyCollection(Collection collection) {

        boolean result = false;

        if (CollectionUtils.isEmpty(collection)) {
            result = true;
        }

        return result;
    }

    /**
     * 检查是否不为空集合
     *
     * @param collection 需要判断的集合
     * @return 检查结果
     */
    public static boolean isNotEmptyCollection(Collection collection) {
        return !isEmptyCollection(collection);
    }

    /**
     * 判断两个对象是否相等
     *
     * @param a 需要判断的对象
     * @param b 需要判断的对象
     * @return 判断结果
     * @throws IllegalArgumentException 无效的参数异常
     */
    public static <T> boolean equals(T a, T b) {

        if (a != null && b != null) {
            if (!Objects.equals(a.getClass(), b.getClass())) {
                throw new IllegalArgumentException();
            }
        }

        return Objects.equals(a, b);
    }

    /**
     * 判断两个对象是否不相等
     *
     * @param a 需要判断的对象
     * @param b 需要判断的对象
     * @return 判断结果
     */
    public static <T> boolean notEquals(T a, T b) {
        return !equals(a, b);
    }

    /**
     * 转换null对象
     *
     * @param object 转换前的对象
     * @return 转换后对象
     */
    public static BigDecimal nullToObject(BigDecimal object) {
        return ObjectUtil.isEmpty(object) ? BigDecimal.ZERO : object;
    }

    /**
     * 转换null对象
     *
     * @param object 转换前的对象
     * @return 转换后对象
     */
    public static String nullToObject(String object) {
        return ObjectUtil.isEmpty(object) ? StringUtil.BLANK : object;
    }


    /**
     * 转换Long对象
     *
     * @param object 转换前的对象
     * @return 转换后对象
     */
    public static Long toLong(Object object) {
        return Long.valueOf(String.valueOf(object));
    }

    /**
     * 转换Timestamp对象
     *
     * @param object 转换前的对象
     * @return 转换后对象
     */
    public static Timestamp toTimestamp(Object object) {
        return Timestamp.valueOf(String.valueOf(object));
    }

    /**
     * 转换BigDecimal对象
     *
     * @param object 转换前的对象
     * @return 转换后对象
     */
    public static BigDecimal toBigDecimal(Object object) {
        return new BigDecimal(String.valueOf(object));
    }


    /**
     * 倒序排列list
     *
     * @param list 排列前的list
     * @return 排列后的list
     */
    public static List reverse(List list) {
        Collections.reverse(list);
        return list;
    }
}