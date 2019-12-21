package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 转账类型
 *
 * @author sunli
 * @date 2019/01/17
 */
@Getter
public enum TransferTypeEnum {

    /**
     * 枚举值
     */
    TOP_UP(1, "充值", "TopUp"),
    WITHDRAWAL_APPLICATION(2, "提币申请", "Withdrawal Application"),
    WITHDRAWAL_APPLICATION_FEE(3, "提币申请-手续费", "Withdrawal Application Fee"),
    WITHDRAWAL_SUCCESSFUL(4, "提币成功", "Withdrawal Successful"),
    WITHDRAWAL_SUCCESSFUL_FEE(5, "提币成功-手续费", "Withdrawal Successful Fee"),
    WITHDRAWAL_FAILURE(6, "提币失败", "Withdrawal Failure"),
    WITHDRAWAL_FAILURE_FEE(7, "提币失败-手续费", "Withdrawal Failure Fee"),
    PAY_ORDER(8, "投资项目", "Join Project"),
    CANCEL_ORDER(9, "投资退出", "Exit Project"),
    INVALIDATE_ORDER(10, "投资失败", "Invalidate Failure"),
    RETURN(11, "释放锁定", "Return"),
    INCOME(12, "项目收益", "Income"),
    REWARD(13, "大盘奖励", "Reward");

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
    TransferTypeEnum(Integer code, String name, String nameEn) {
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
        for (TransferTypeEnum item : TransferTypeEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return LanguageUtil.getTextByLanguage(item.getName(), item.getNameEn());
            }
        }
        return null;
    }
}