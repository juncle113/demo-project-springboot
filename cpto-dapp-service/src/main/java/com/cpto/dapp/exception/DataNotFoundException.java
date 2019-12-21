package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 数据未找到异常
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class DataNotFoundException extends BaseException {

    /**
     * 数据未找到异常
     */
    public DataNotFoundException() {
        super(ErrorEnum.DATA_NOT_FOUND, null);
    }
}