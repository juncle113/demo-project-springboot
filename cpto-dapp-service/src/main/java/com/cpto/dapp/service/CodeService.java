package com.cpto.dapp.service;

import com.cpto.dapp.pojo.dto.SendCodeDTO;

/**
 * 验证码Service
 *
 * @author sunli
 * @date 2018/01/01
 */
public interface CodeService extends BaseService {

    /**
     * 检查验证码
     *
     * @param verifyCase 验证场合（1：注册，2：修改密码）
     * @param sendTo     发送地址
     * @param code       验证码
     */
    void verifyCode(Integer verifyCase, String sendTo, String code);

    /**
     * 发送验证码
     *
     * @param sendCodeDTO 发送验证码信息
     */
    void sendCode(SendCodeDTO sendCodeDTO);
}