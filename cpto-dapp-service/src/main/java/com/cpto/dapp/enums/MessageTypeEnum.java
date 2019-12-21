package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author sunli
 * @date 2019/02/23
 */
@Getter
public enum MessageTypeEnum {

    /**
     * 枚举值
     */
    SYSTEM(1, "系统消息"),
    PAYMENT(2, "投资消息");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    MessageTypeEnum(Integer code, String name) {
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
        for (MessageTypeEnum item : MessageTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}