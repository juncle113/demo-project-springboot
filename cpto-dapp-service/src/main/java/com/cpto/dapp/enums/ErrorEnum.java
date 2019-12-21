package com.cpto.dapp.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误信息
 *
 * @author sunli
 * @date 2018/12/07
 */
@Getter
public enum ErrorEnum {

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    ACTION_FAILED(10002, "操作失败"),
    SYSTEM_ERROR(10003, "系统繁忙，请稍后重试"),
    REQUEST_LIMIT(10004, "请求过于频繁，请稍后再试"),

    /* 数据错误：20001-299999 */
    DATA_NOT_FOUND(20001, "数据未找到"),
    DATA_EXISTED(20002, "数据已存在"),
    DATA_EXPIRED(20003, "数据已被修改，请重新取得"),

    /* 业务错误：30001-39999 */
    BALANCE_NOT_ENOUGH(30001, "余额不足"),
    TRANSFER_FAILED(30002, "转账失败"),
    DELETE_PROJECT_FAILED(30003, "已存在投资，无法删除该项目"),
    CREATE_ORDER_FAILED_1(30004, "项目当前状态不可投资，操作失败"),
    CREATE_ORDER_FAILED_2(30005, "未满足最低投资数额，操作失败"),
    CREATE_ORDER_FAILED_3(30006, "未满足最低持仓数额，操作失败"),
    CREATE_ORDER_FAILED_4(30007, "未满足注册时间要求，操作失败"),
    CREATE_ORDER_FAILED_5(30008, "超过项目参与人数上限，操作失败"),
    CANCEL_ORDER_FAILED_1(30009, "项目未到评估完成阶段，无法退出投资"),
    CANCEL_ORDER_FAILED_2(30010, "当前订单状态无法撤销"),
    GRANT_INCOME_FAILED_1(30011, "当前项目状态无法发放收益"),
    GRANT_INCOME_FAILED_2(30012, "项目募集投资数额为0，无法发放收益"),
    PROJECT_NO_EXISTED(30013, "项目编号已存在，请重新输入"),
    ORDER_ERROR(30014, "订单信息有误，请联系客服"),
    AMOUNT_ERROR_1(30015, "数额不能高于上限"),
    AMOUNT_ERROR_2(30016, "数额不能低于下限"),
    AMOUNT_ERROR_3(30017, "数额必须大于0"),
    CHAIN_ACCOUNT_CREATE_FAILED(30018, "区块链账户创建失败"),
    CHAIN_ACCOUNT_RECOVERY_FAILED(30019, "区块链账户回收失败"),

    /* 其他错误：40001-49999 */
    PERMISSION_NO_ACCESS(40001, "无访问权限或认证已超时，请重新登录"),
    NOT_LOGGED_IN(40002, "用户未登录"),
    LOGIN_ERROR(40003, "账号或密码错误"),
    ACCOUNT_FORBIDDEN(40004, "账号已被禁用"),
    PAY_PASSWORD_NOT_SET(40005, "请先设置支付密码"),
    PAY_PASSWORD_ERROR(40006, "支付密码错误，请重试"),
    VERIFICATION_CODE_ERROR(40007, "验证码错误"),
    VERIFICATION_CODE_INVALID(40008, "验证码已失效，请重新取得"),
    INVITATION_CODE_ERROR(40009, "邀请码错误"),
    AREA_CODE_NOT_SET(40010, "请输入手机号的归属地代码"),
    USER_EXISTED(40011, "用户已存在，请重新输入");

    private Integer code;
    private String message;

    /**
     * 错误信息的构造方法
     *
     * @param code    错误编码
     * @param message 错误提示
     */
    ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static void main(String[] args) {
        // 校验重复的code值
        checkCode();
    }

    /**
     * 校验重复的code值
     */
    private static void checkCode() {
        ErrorEnum[] errorEnums = ErrorEnum.values();
        List codeList = new ArrayList();
        for (ErrorEnum item : errorEnums) {
            if (codeList.contains(item.getCode())) {
                System.out.println(item.getCode());
            } else {
                codeList.add(item.getCode());
            }
        }
    }
}