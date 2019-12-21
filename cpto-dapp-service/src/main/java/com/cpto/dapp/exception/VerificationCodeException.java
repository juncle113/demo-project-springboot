package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 验证码验证异常
 *
 * @author sunli
 * @date 2018/12/31
 */
@Data
public class VerificationCodeException extends BaseException {

    public VerificationCodeException() {
        super(ErrorEnum.VERIFICATION_CODE_ERROR, null);
    }
}
