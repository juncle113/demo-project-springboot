package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 反馈状态
 *
 * @author sunli
 * @date 2019/01/29
 */
@Getter
public enum FeedbackStatusEnum {

    /**
     * 枚举值
     */
    UNTREATED(1, "未处理"),
    PROCESSING(2, "处理中"),
    PROCESSED(3, "已处理");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    FeedbackStatusEnum(Integer code, String name) {
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
        for (FeedbackStatusEnum item : FeedbackStatusEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}