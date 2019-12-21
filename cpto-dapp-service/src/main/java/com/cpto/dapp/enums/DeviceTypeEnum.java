package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 设备类型
 *
 * @author sunli
 * @date 2019/01/07
 */
@Getter
public enum DeviceTypeEnum {

    /**
     * 枚举值
     */
    ANDROID(1, "Android"),
    IOS(2, "iOS");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    DeviceTypeEnum(Integer code, String name) {
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
        for (DeviceTypeEnum item : DeviceTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}