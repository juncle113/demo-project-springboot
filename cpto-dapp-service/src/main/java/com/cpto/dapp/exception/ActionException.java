package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 操作异常
 *
 * @author sunli
 * @date 2019/01/23
 */
@Data
public class ActionException extends BaseException {

    public ActionException() {
        super(ErrorEnum.ACTION_FAILED, null);
    }
}