package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 收益状态类型
 *
 * @author sunli
 * @date 2019/02/22
 */
@Getter
public enum IncomeStatusEnum {

    /**
     * 枚举值
     */
    START(1, "发放开始"),
    END(2, "发放完成"),
    INVALID(3, "无效");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    IncomeStatusEnum(Integer code, String name) {
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
        for (IncomeStatusEnum item : IncomeStatusEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}