package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.helper.MailHelper;
import com.cpto.dapp.common.helper.SmsHelper;
import com.cpto.dapp.common.util.*;
import com.cpto.dapp.domain.LogCode;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.enums.VerifyCaseEnum;
import com.cpto.dapp.enums.VerifyTypeEnum;
import com.cpto.dapp.exception.ActionException;
import com.cpto.dapp.exception.ParameterException;
import com.cpto.dapp.exception.VerificationCodeException;
import com.cpto.dapp.exception.VerificationCodeInvalidException;
import com.cpto.dapp.pojo.dto.SendCodeDTO;
import com.cpto.dapp.pojo.vo.CodeVO;
import com.cpto.dapp.repository.LogCodeRepository;
import com.cpto.dapp.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 验证码ServiceImpl
 *
 * @author sunli
 * @date 2019/01/03
 */
@Service
public class CodeServiceImpl extends BaseServiceImpl implements CodeService {

    @Autowired
    private LogCodeRepository logCodeRepository;

    @Autowired
    private SmsHelper smsHelper;

    @Autowired
    private MailHelper mailHelper;

    @Value("${custom.code.enable}")
    private Boolean codeEnable;

    @Value("${custom.code.length}")
    private Integer codeLength;

    @Value("${custom.code.timeout}")
    private Integer codeTimeout;

    @Value("${custom.code.message-template}")
    private String codeMessageTemplate;

    @Value("${custom.code.message-template-en}")
    private String codeMessageTemplateEn;

    @Value("${custom.sms.enable}")
    private Boolean smsEnable;

    @Value("${custom.mail.enable}")
    private Boolean mailEnable;

    @Value("${spring.mail.user-name}")
    private String mailAccount;

    @Value("${custom.mail.code.subject}")
    private String mailCodeSubject;

    @Value("${custom.mail.code.subject-en}")
    private String mailCodeSubjectEn;

    /**
     * 检查验证码
     *
     * @param verifyCase 验证场合（1：注册，2：修改密码）
     * @param sendTo     发送地址
     * @param code       验证码
     */
    @Override
    public void verifyCode(Integer verifyCase, String sendTo, String code) {

        /* 1.判断配置中是否开启验证功能 */
        if (!codeEnable) {
            return;
        }

        /* 2.检查参数 */
        if (ObjectUtil.isEmpty(verifyCase) || ObjectUtil.isEmpty(sendTo) || ObjectUtil.isEmpty(code)) {
            throw new ParameterException();
        }

        /* 3.取得验证码记录 */
        LogCode logCode = logCodeRepository.findByVerifyCaseAndSendToAndCode(verifyCase, sendTo, code);

        /* 4.检查验证码 */
        /* 4.1.验证码错误的场合 */
        if (ObjectUtil.isEmpty(logCode)) {
            throw new VerificationCodeException();
        }

        /* 4.2.验证码失效的场合 */
        if (ObjectUtil.notEquals(logCode.getStatus(), StatusEnum.VALID.getCode())
                || DateUtil.isExpires(logCode.getExpiresTime())) {
            throw new VerificationCodeInvalidException();
        }

        /* 5.更新验证码记录 */
        logCode.setStatus(StatusEnum.INVALID.getCode());
        logCode.setModifiedBy(null);
        logCode.setModifiedTime(DateUtil.now());
        logCodeRepository.save(logCode);
    }

    /**
     * 发送验证码
     *
     * @param sendCodeDTO 发送验证码信息
     */
    @Override
    public void sendCode(SendCodeDTO sendCodeDTO) {

        String sendTo = null;

        /* 1.检查参数 */
        if (ObjectUtil.equals(sendCodeDTO.getVerifyType(), VerifyTypeEnum.PHONE.getCode())) {
            if (ObjectUtil.isEmpty(sendCodeDTO.getAreaCode()) || ObjectUtil.isEmpty(sendCodeDTO.getAreaCode())) {
                throw new ParameterException();
            } else {
                sendTo = sendCodeDTO.getAreaCode().concat(sendCodeDTO.getPhone());
            }
        } else if (ObjectUtil.equals(sendCodeDTO.getVerifyType(), VerifyTypeEnum.EMAIL.getCode())) {
            if (ObjectUtil.isEmpty(sendCodeDTO.getEmail())) {
                throw new ParameterException();
            } else {
                sendTo = sendCodeDTO.getEmail();
            }
        }

        /* 2.生成验证码(根据配置文件指定位数) */
        String code = IdUtil.generateNumberCode(codeLength);

        /* 3.保存短信发送记录 */
        saveCodeLog(sendCodeDTO.getVerifyType(), sendCodeDTO.getVerifyCase(), sendTo, code);

        /* 4.发送验证信息 */
        sendCodeMessage(sendCodeDTO.getVerifyType(), sendTo, code);
    }

    /**
     * 保存短信发送记录
     *
     * @param verifyType 验证方式
     * @param verifyCase 验证场合
     * @param sendTo     发送地址
     * @param code       验证码
     */
    private void saveCodeLog(Integer verifyType, Integer verifyCase, String sendTo, String code) {

        LogCode logCode = new LogCode();

        logCode.setVerifyType(verifyType);
        logCode.setVerifyCase(verifyCase);
        logCode.setSendTo(sendTo);
        logCode.setCode(code);
        logCode.setStatus(StatusEnum.VALID.getCode());
        logCode.setCreatedTime(DateUtil.now());
        logCode.setExpiresTime(DateUtil.expiresTime(logCode.getCreatedTime(),
                DateUtil.minuteToMillisecond(codeTimeout)));

        logCodeRepository.save(logCode);
    }

    /**
     * 发送验证信息
     *
     * @param verifyType 验证方式
     * @param sendTo     发送地址
     * @param code       验证码
     */
    private void sendCodeMessage(Integer verifyType, String sendTo, String code) {

        /* 1.判断配置中是否开启发送功能 */
        if (!codeEnable) {
            return;
        }

        /* 2.取得对应语言的验证信息内容 */
        String message = LanguageUtil.getTextByLanguage(codeMessageTemplate, codeMessageTemplateEn);

        /* 3.填写验证信息 */
        message = StringUtil.messageFormat(message, code, String.valueOf(codeTimeout));

        /* 4.根据验证方式发送验证信息 */
        if (ObjectUtil.equals(verifyType, VerifyTypeEnum.PHONE.getCode())) {
            /* 4.1.发送手机短信 */
            // 判断配置中是否开启发送功能
            if (!smsEnable) {
                return;
            }

            try {
                smsHelper.send(sendTo, message);
            } catch (Exception e) {
                throw new ActionException();
            }
        } else if (ObjectUtil.equals(verifyType, VerifyTypeEnum.EMAIL.getCode())) {
            /* 4.2.发送电子邮件 */
            // 判断配置中是否开启发送功能
            if (!mailEnable) {
                return;
            }

            try {
                mailHelper.sendSimpleMail(mailAccount, sendTo, LanguageUtil.getTextByLanguage(mailCodeSubject, mailCodeSubjectEn), message);
            } catch (Exception e) {
                throw new ActionException();
            }
        } else {
            throw new ParameterException();
        }
    }

    /**
     * 编辑验证码VO
     *
     * @param logCode 验证码记录
     * @return 验证码VO
     */
    private CodeVO editCodeVO(LogCode logCode) {

        CodeVO codeVO = new CodeVO();

        codeVO.setId(logCode.getId());
        codeVO.setVerifyType(logCode.getVerifyType());
        codeVO.setVerifyTypeName(VerifyTypeEnum.getNameByCode(logCode.getVerifyType()));
        codeVO.setSendTo(logCode.getSendTo());
        codeVO.setVerifyCase(logCode.getVerifyCase());
        codeVO.setVerifyCaseName(VerifyCaseEnum.getNameByCode(logCode.getVerifyCase()));
        codeVO.setStatus(logCode.getStatus());
        codeVO.setStatusName(StatusEnum.getNameByCode(logCode.getStatus()));
        codeVO.setCreatedTime(logCode.getCreatedTime());
        codeVO.setTimeout(codeTimeout);
        codeVO.setExpiresTime(logCode.getExpiresTime());

        return codeVO;
    }
}