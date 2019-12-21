package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 管理员登录异常
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class LoginException extends BaseException {

    public LoginException() {
        super(ErrorEnum.LOGIN_ERROR, null);
    }
}
