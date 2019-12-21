package com.cpto.dapp.common.util;

import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.text.MessageFormat;

/**
 * 字符串工具类
 *
 * @author sunli
 * @date 2018/12/18
 */
public class StringUtil {

    /**
     * 半角空格
     */
    public static final String HALF_SPACE = " ";

    /**
     * 空字符串
     */
    public static final String BLANK = "";

    /**
     * null字符串
     */
    public static final String NULL = "null";

    /**
     * 格式化message
     *
     * @param message 需要被格式化的message
     * @param param   填入的参数
     * @return 格式化后的message
     */
    public static String messageFormat(String message, String... param) {
        return MessageFormat.format(message, param);
    }

    /**
     * 返回截取长度内的字符串（没有长度异常）
     *
     * @param str          需要被截取的字符串
     * @param length       截取长度
     * @param defaultValue 字符串为空时默认值
     * @return 截取长度内的字符串
     */
    public static String safetySubString(String str, int length, String defaultValue) {

        if (ObjectUtil.isEmpty(str)) {
            return defaultValue;
        }

        // 字符串不足截取长度的场合，返回全部字符串
        int subLength = str.length() > length ? length : str.length();
        return str.substring(0, subLength);
    }

    /**
     * 返回省略显示内容
     *
     * @param str          需要被截取的字符串
     * @param length       截取长度
     * @param defaultValue 字符串为空时默认值
     * @return 省略显示内容
     */
    public static String ellipsis(String str, int length, String defaultValue) {
        String result = safetySubString(str, length, defaultValue);
        return ObjectUtil.isNotEmpty(result) ? result.concat("...") : result;
    }

    /**
     * 返回md5加密后的字符串
     *
     * @param str 需要被加密的字符串
     * @return md5加密后的字符串
     */
    public static String toMd5(String str) {
        return DigestUtils.md5DigestAsHex(StringUtil.nullToBlank(str).getBytes());
    }

    /**
     * 返回base64转码后的字符串
     *
     * @param str 需要被转码的字符串
     * @return base64转码后的字符串
     */
    public static String encodeBase64(String str) {
        return Base64Utils.encodeToString(str.getBytes());
    }

    /**
     * 返回base64解码后的字符串
     *
     * @param str 需要被解码的字符串
     * @return base64解码后的字符串
     * @throws IllegalArgumentException 无效参数异常
     */
    public static String decodeBase64(String str) throws IllegalArgumentException {
        return new String(Base64Utils.decodeFromString(str));
    }

    /**
     * 将null转换为空字符串
     *
     * @param str 需要被转换的字符串
     * @return 返回转换后的字符串
     */
    public static String nullToBlank(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }
}