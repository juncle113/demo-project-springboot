package com.cpto.dapp.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * 消息工具类
 *
 * @author sunli
 * @date 2019/03/15
 */
@Component
public class MessageUtil {

    @Autowired
    private MessageSource messageSource;

    public static MessageUtil messageUtil;

    @PostConstruct
    public void init() {
        messageUtil = this;
        messageUtil.messageSource = this.messageSource;
    }

    /**
     * 取得语言区分对应的消息
     *
     * @param code  消息code
     * @param param 消息参数
     * @return 消息
     */
    public static String getMessage(String code, String... param) {

        // 手动创建
        // ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // messageSource.setCacheSeconds(-1);
        // messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // messageSource.setBasenames("/messages");

        Locale locale = LocaleContextHolder.getLocale();
        return messageUtil.messageSource.getMessage(code, param, locale);
    }
}