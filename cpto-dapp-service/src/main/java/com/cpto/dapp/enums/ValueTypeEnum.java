package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 价值设置类型
 *
 * @author sunli
 * @date 2019/01/10
 */
@Getter
public enum ValueTypeEnum {

    /**
     * 枚举值
     */
    VALUE_SUMMARY(1, "价值概要"),
    TOTAL_ALLOCATION(2, "总量分配"),
    CIRCULATION_SOURCE(3, "流通来源");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    ValueTypeEnum(Integer code, String name) {
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
        for (ValueTypeEnum item : ValueTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}