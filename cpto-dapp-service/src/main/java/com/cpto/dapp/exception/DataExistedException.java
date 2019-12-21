package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 数据已存在异常
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class DataExistedException extends BaseException {

    public DataExistedException(String param) {
        super(ErrorEnum.DATA_EXISTED, param);
    }

    public DataExistedException() {
        this(null);
    }

}