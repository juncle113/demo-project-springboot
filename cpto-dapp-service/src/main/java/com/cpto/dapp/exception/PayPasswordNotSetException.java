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
public class PayPasswordNotSetException extends BaseException {

    public PayPasswordNotSetException() {
        super(ErrorEnum.PAY_PASSWORD_NOT_SET, null);
    }
}