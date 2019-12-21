package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 反馈类型
 *
 * @author sunli
 * @date 2019/02/15
 */
@Getter
public enum FeedbackTypeEnum {

    /**
     * 枚举值
     */
    APPLY(1, "申请"),
    COMPLAINT(2, "投诉");

    private Integer code;
    private String name;

    /**
     * 构造方法
     *
     * @param code 类型编码
     * @param name 类型名称
     */
    FeedbackTypeEnum(Integer code, String name) {
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
        for (FeedbackTypeEnum item : FeedbackTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return item.getName();
            }
        }
        return null;
    }
}