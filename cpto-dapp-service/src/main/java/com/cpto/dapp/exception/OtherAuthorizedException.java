package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 鉴权异常
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class OtherAuthorizedException extends BaseException {

    public OtherAuthorizedException() {
        super(ErrorEnum.ACCOUNT_FORBIDDEN, null);
    }
}
