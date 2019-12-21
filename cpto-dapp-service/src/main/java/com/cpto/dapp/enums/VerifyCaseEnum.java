package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 验证场合
 *
 * @author sunli
 * @date 2018/12/31
 */
@Getter
public enum VerifyCaseEnum {

    /**
     * 枚举值
     */
    REGISTER(1, "注册场合"),
    PASSWORD(2, "修改登录密码场合"),
    PAY_PASSWORD(3, "修改支付密码场合"),
    PHONE(4, "绑定手机号场合"),
    EMAIL(5, "绑定邮箱场合");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    VerifyCaseEnum(Integer code, String name) {
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
        for (VerifyCaseEnum item : VerifyCaseEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}