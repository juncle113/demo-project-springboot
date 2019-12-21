package com.cpto.dapp.exception;

import com.cpto.dapp.enums.ErrorEnum;
import lombok.Data;

/**
 * 数据已被修改异常
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
public class DataExpiredException extends BaseException {

    /**
     * 数据已被修改异常
     *
     * @param param 异常信息参数
     */
    public DataExpiredException(String param) {
        super(ErrorEnum.DATA_EXPIRED, param);
    }

    /**
     * 数据未找到异常
     */
    public DataExpiredException() {
        this(null);
    }
}
