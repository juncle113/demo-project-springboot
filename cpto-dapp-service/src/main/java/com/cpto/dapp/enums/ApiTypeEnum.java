package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 接口类型
 *
 * @author sunli
 * @date 2019/03/19
 */
@Getter
public enum ApiTypeEnum {

    /**
     * 枚举值
     */
    APP(1, "前台应用"),
    MANAGER(2, "后台管理");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    ApiTypeEnum(Integer code, String name) {
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
        for (ApiTypeEnum item : ApiTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}