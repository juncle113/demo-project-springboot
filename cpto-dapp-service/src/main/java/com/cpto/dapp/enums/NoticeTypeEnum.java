package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 公告类型
 *
 * @author sunli
 * @date 2019/02/18
 */
@Getter
public enum NoticeTypeEnum {

    /**
     * 枚举值
     */
    PROJECT(1, "项目公告"),
    RETURN(2, "回报公告");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    NoticeTypeEnum(Integer code, String name) {
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
        for (NoticeTypeEnum item : NoticeTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}