package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 订单状态类型
 * 对应各张表的status
 *
 * @author sunli
 * @date 2018/12/28
 */
@Getter
public enum OrderStatusEnum {

    /**
     * 枚举值
     */
    PAID(1, "已支付", "Paid"),
    CANCELED(2, "已取消", "Canceled"),
    COMPLETED(3, "已完成", "Completed"),
    INVALID(4, "已失效", "Invalid");

    private Integer code;
    private String name;
    private String nameEn;

    /**
     * 构造方法
     *
     * @param code   编码
     * @param name   名称（中文）
     * @param nameEn 名称（英文）
     */
    OrderStatusEnum(Integer code, String name, String nameEn) {
        this.code = code;
        this.name = name;
        this.nameEn = nameEn;
    }

    /**
     * 取得类型名称
     *
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(Integer code) {
        for (OrderStatusEnum item : OrderStatusEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return LanguageUtil.getTextByLanguage(item.getName(), item.getNameEn());
            }
        }
        return null;
    }
}