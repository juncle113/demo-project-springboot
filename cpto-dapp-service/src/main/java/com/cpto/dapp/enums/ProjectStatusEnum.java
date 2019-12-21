package com.cpto.dapp.enums;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import lombok.Getter;

/**
 * 项目状态类型
 *
 * @author sunli
 * @date 2019/01/19
 */
@Getter
public enum ProjectStatusEnum {

    /**
     * 枚举值
     */
    READY(1, "准备阶段", "Ready"),
    START(2, "开始募集", "Start"),
    SUCCESS_1(3, "募集成功", "Recruitment Success"),
    FAILURE_1(4, "募集失败", "Recruitment Failure"),
    S1(5, "S1进行中", "S1 In Progress"),
    S1_END(6, "S1完成", "S1 Completed"),
    S2(7, "S2进行中", "S2 In Progress"),
    S2_END(8, "S2完成", "S2 Completed"),
    S3(9, "S3进行中", "S3 In Progress"),
    S3_END(10, "S3完成", "S3 Completed"),
    SUCCESS_2(11, "投资成功", "Investment Success"),
    FAILURE_2(12, "投资失败", "Investment Failure"),
    OPERATING(13, "运营中", "Operating"),
    INVALID(14, "无效", "Invalid");

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
    ProjectStatusEnum(Integer code, String name, String nameEn) {
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
        for (ProjectStatusEnum item : ProjectStatusEnum.values()) {
            if (ObjectUtil.equals(code, item.getCode())) {
                return LanguageUtil.getTextByLanguage(item.getName(), item.getNameEn());
            }
        }
        return null;
    }
}