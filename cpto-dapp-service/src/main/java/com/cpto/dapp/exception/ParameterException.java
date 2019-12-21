package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 参数异常
 *
 * @author sunli
 * @date 2019/01/03
 */
@Data
public class ParameterException extends BaseException {

    public ParameterException() {
        super(ErrorEnum.PARAM_IS_INVALID, null);
    }
}
