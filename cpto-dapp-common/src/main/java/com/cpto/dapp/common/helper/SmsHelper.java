package com.cpto.dapp.common.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cpto.dapp.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 短信Helper
 *
 * @author sunli
 * @date 2019/01/07
 */
@Component
@Slf4j
public class SmsHelper {

    @Value("${custom.sms.url}")
    private String smsUrl;

    @Value("${custom.sms.account}")
    private String smsAccount;

    @Value("${custom.sms.password}")
    private String smsPassword;

    /**
     * 发送短信
     *
     * @param phone   手机号
     * @param message 内容
     * @throws Exception 处理异常
     */
    @Async
    public void send(@Pattern(regexp = "^[0-9]*$", message = "手机号必须为数字") @NotBlank(message = "手机号不能为空") String phone,
                     @NotBlank(message = "内容不能为空") String message) throws Exception {

        // 请求地址
        String url = smsUrl;

        // API账号，50位以内。必填
        String account = smsAccount;

        // API账号对应密钥，联系客服取得。必填
        String password = smsPassword;

        // 短信内容。长度不能超过536个字符。必填
        String msg = message;

        // 手机号码，格式(区号+手机号码)，例如：8615800000000，其中86为中国的区号，区号前不使用00开头,15800000000为接收短信的真实手机号码。5-20位。必填
        String mobile = phone;

        // 组装请求参数
        JSONObject map = new JSONObject();
        map.put("account", account);
        map.put("password", password);
        map.put("msg", msg);
        map.put("mobile", mobile);

        String params = map.toString();

        log.info("请求参数为:" + params);
        try {
            String result = HttpUtil.post(url, params);

            log.info("返回参数为:" + result);

            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.get("code").toString();
            String msgid = jsonObject.get("msgid").toString();
            String error = jsonObject.get("error").toString();

            log.info("状态码:" + code + ",状态码说明:" + error + ",消息id:" + msgid);
        } catch (Exception e) {
            log.error("短信发送失败：" + e);
            throw e;
        }
    }
}