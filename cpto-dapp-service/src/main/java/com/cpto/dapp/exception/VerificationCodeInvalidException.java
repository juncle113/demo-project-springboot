package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 验证码验证异常
 *
 * @author sunli
 * @date 2019/01/03
 */
@Data
public class VerificationCodeInvalidException extends BaseException {

    public VerificationCodeInvalidException() {
        super(ErrorEnum.VERIFICATION_CODE_INVALID, null);
    }
}
