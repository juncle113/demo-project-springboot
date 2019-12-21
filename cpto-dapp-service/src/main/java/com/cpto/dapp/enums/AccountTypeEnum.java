package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 账户类型
 *
 * @author sunli
 * @date 2019/01/15
 */
@Getter
public enum AccountTypeEnum {

    /**
     * 枚举值
     */
    AVAILABLE(1, "可用账户", "Available Account"),
    LOCKED(2, "锁定账户", "Locked Account"),
    APPROVED(3, "待审核账户", "Approved Account");

    private Integer code;
    private String name;
    private String nameEn;

    /**
     * 构造方法
     *
     * @param code   类型编码
     * @param name   类型名称（中文）
     * @param nameEn 类型名称（英文）
     */
    AccountTypeEnum(Integer code, String name, String nameEn) {
        this.code = code;
        this.name = name;
        this.nameEn = nameEn;
    }

    /**
     * 取得类型名称
     *
     * @param code 类型编码
     * @return 类型名称
     */
    public static String getNameByCode(Integer code) {
        for (AccountTypeEnum item : AccountTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return LanguageUtil.getTextByLanguage(item.getName(), item.getNameEn());
            }
        }
        return null;
    }
}