package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 业务关联类型
 *
 * @author sunli
 * @date 2019/01/23
 */
@Getter
public enum RelationTypeEnum {

    /**
     * 枚举值
     */
    TOP_UP(1, "充值"),
    WITHDRAWAL_APPLICATION(2, "提币申请"),
    ORDER(3, "订单"),
    INCOME(4, "收益记录"),
    PROJECT(5, "项目"),
    NOTICE(6, "公告");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    RelationTypeEnum(Integer code, String name) {
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
        for (RelationTypeEnum item : RelationTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}