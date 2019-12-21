package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 鉴权异常
 *
 * @author sunli
 * @date 2019/01/30
 */
@Data
public class PayPasswordErrorException extends BaseException {

    public PayPasswordErrorException() {
        super(ErrorEnum.PAY_PASSWORD_ERROR, null);
    }
}
