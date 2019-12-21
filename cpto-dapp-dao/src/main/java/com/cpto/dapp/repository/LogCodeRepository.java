package com.cpto.dapp.repository;


import com.cpto.dapp.domain.LogCode;

/**
 * 验证码记录Repository
 *
 * @author sunli
 * @date 2018/12/31
 */
public interface LogCodeRepository extends BaseRepository<LogCode, Long> {

    /**
     * 查询验证码信息
     *
     * @param verifyCase 验证场合
     * @param sendTo     发送地址
     * @param code       验证码
     * @return 验证码信息
     */
    LogCode findByVerifyCaseAndSendToAndCode(Integer verifyCase, String sendTo, String code);
}