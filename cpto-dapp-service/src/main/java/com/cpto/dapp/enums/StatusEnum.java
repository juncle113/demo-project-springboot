package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 状态类型
 * 对应各张表的status
 *
 * @author sunli
 * @date 2018/12/28
 */
@Getter
public enum StatusEnum {

    /**
     * 枚举值
     */
    VALID(1, "有效"),
    INVALID(2, "无效");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    StatusEnum(Integer code, String name) {
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
        for (StatusEnum item : StatusEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}