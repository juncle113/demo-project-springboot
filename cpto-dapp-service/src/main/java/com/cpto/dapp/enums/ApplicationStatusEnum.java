package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 申请状态
 *
 * @author sunli
 * @date 2019/03/12
 */
@Getter
public enum ApplicationStatusEnum {

    /**
     * 枚举值
     */
    UNTREATED(1, "未处理"),
    SUCCESS(2, "成功"),
    FAILED(3, "失败");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    ApplicationStatusEnum(Integer code, String name) {
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
        for (ApplicationStatusEnum item : ApplicationStatusEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}