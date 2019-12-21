package com.cpto.dapp.exception;

import com.cpto.dapp.common.util.MessageUtil;
import com.cpto.dapp.common.util.StringUtil;
import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * Base异常
 *
 * @author sunli
 * @date 2018/12/14
 */
@Data
public class BaseException extends RuntimeException {

    private ErrorInfo errorInfo;

    public BaseException(ErrorEnum errorEnum, String... param) {
        super(errorEnum.getCode() + ":" + StringUtil.messageFormat(errorEnum.getMessage(), param));
        this.errorInfo = new ErrorInfo(errorEnum.getCode(), MessageUtil.getMessage(String.valueOf(errorEnum.getCode()), param));
    }
}