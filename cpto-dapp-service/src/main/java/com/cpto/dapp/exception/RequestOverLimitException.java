package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 请求超过限制异常
 *
 * @author sunli
 * @date 2019/01/07
 */
@Data
public class RequestOverLimitException extends BaseException {

    public RequestOverLimitException() {
        super(ErrorEnum.REQUEST_LIMIT, null);
    }
}
