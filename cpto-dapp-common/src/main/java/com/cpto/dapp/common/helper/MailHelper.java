package com.cpto.dapp.common.helper;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 邮件Helper
 *
 * @author sunli
 * @date 2019/01/10
 */
@Component
@Validated
@Slf4j
public class MailHelper {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JavaMailSender javaMailSender;

    /**
     * 发送简单邮件
     *
     * @param from    发送邮箱地址
     * @param to      接收邮箱地址
     * @param subject 主题
     * @param message 内容
     * @throws Exception 处理异常
     */
    @Async
    public void sendSimpleMail(@Email(message = "发送邮箱地址格式错误") @NotBlank(message = "发送邮箱地址不能为空") String from,
                               @Email(message = "接收邮箱地址格式错误") @NotBlank(message = "接收邮箱地址不能为空") String to,
                               @NotBlank(message = "主题不能为空") String subject,
                               @NotBlank(message = "内容不能为空") String message) throws Exception {

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            // 邮件发送人
            simpleMailMessage.setFrom(from);
            // 邮件接收人
            simpleMailMessage.setTo(to);
            // 邮件主题
            simpleMailMessage.setSubject(subject);
            // 邮件内容
            simpleMailMessage.setText(message);

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.error("邮件发送失败");
            log.error(e.getMessage());
            throw e;
        }
    }
}