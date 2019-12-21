package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 业务异常
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class BusinessException extends BaseException {

    public BusinessException(ErrorEnum e) {
        super(e, null);
    }
}
