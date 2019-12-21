package com.cpto.dapp.common.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 语言工具类
 *
 * @author sunli
 * @date 2019/01/21
 */
public class LanguageUtil {

    /**
     * 取得语言区分对应的信息
     *
     * @param text   中文信息
     * @param textEn 英文信息
     * @return 计算结果
     */
    public static String getTextByLanguage(String text, String textEn) {

        /* 1.取得请求接口中header部的语言参数（Accept-Language） */
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage();

        /* 2.根据语言设置返回对应文本内容 */
        String result;

        if (ObjectUtil.equals(lang, Locale.ENGLISH.getLanguage())) {
            result = textEn;
        } else {
            result = text;
        }

        return result;
    }
}