package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 验证方式
 *
 * @author sunli
 * @date 2018/12/31
 */
@Getter
public enum VerifyTypeEnum {

    /**
     * 枚举值
     */
    PHONE(1, "通过手机短信验证"),
    EMAIL(2, "通过电子邮件验证");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    VerifyTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 取得类型名称
     *
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(Integer code) {
        for (VerifyTypeEnum item : VerifyTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}