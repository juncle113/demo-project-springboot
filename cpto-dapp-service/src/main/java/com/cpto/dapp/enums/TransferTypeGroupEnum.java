package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 转账类型分组
 *
 * @author sunli
 * @date 2019/01/28
 */
@Getter
public enum TransferTypeGroupEnum {

    /**
     * 枚举值
     */
    PAY(1, "支付", "Pay"),
    INCOME(2, "收益", "Income"),
    TOP_UP(3, "充值", "TopUp"),
    WITHDRAW(4, "提币", "Withdraw");

    private Integer code;
    private String name;
    private String nameEn;

    /**
     * 构造方法
     *
     * @param code   类型编码
     * @param name   类型名称（中文）
     * @param nameEn 类型名称（英文）
     */
    TransferTypeGroupEnum(Integer code, String name, String nameEn) {
        this.code = code;
        this.name = name;
        this.nameEn = nameEn;
    }

    /**
     * 取得类型名称
     *
     * @param code 类型编码
     * @return 类型名称
     */
    public static String getNameByCode(Integer code) {
        for (TransferTypeGroupEnum item : TransferTypeGroupEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return LanguageUtil.getTextByLanguage(item.getName(), item.getNameEn());
            }
        }
        return null;
    }
}