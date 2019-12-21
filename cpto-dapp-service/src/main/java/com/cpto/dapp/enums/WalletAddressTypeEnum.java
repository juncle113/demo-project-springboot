package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 钱包地址类型
 *
 * @author sunli
 * @date 2019/01/15
 */
@Getter
public enum WalletAddressTypeEnum {

    /**
     * 枚举值
     */
    TOP_UP(1, "充值"),
    WITHDRAW(2, "提币");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    WalletAddressTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 取得类型名称
     *
     * @param code 类型编码
     * @return 类型名称
     */
    public static String getNameByCode(Integer code) {
        for (WalletAddressTypeEnum item : WalletAddressTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}