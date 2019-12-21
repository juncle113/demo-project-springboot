package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 项目信息
 *
 * @author sunli
 * @date 2019/01/24
 */
@ApiModel(value = "ProjectDTO", description = "项目信息")
@Data
public class ProjectDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "项目编号", example = "20190401", required = true)
    @NotBlank(message = "项目编号不能为空")
    @Size(max = 20, message = "项目编号必须为20位以内字符")
    private String no;

    @ApiModelProperty(position = 2, value = "项目名称（中文）", example = "特斯拉油田", required = true)
    @NotBlank(message = "项目名称（中文）不能为空")
    @Size(max = 50, message = "项目名称（中文）必须为50位以内字符")
    private String nameZh;

    @ApiModelProperty(position = 3, value = "项目名称（英文）", example = "nameEn", required = true)
    @NotBlank(message = "项目名称（英文）不能为空")
    @Size(max = 50, message = "项目名称（英文）必须为50位以内字符")
    private String nameEn;

    @ApiModelProperty(position = 4, value = "项目简介（中文）", example = "项目简介内容", required = true)
    @NotBlank(message = "项目简介（中文）不能为空")
    @Size(max = 100, message = "项目简介（英文）必须为100位以内字符")
    private String summaryZh;

    @ApiModelProperty(position = 5, value = "项目简介（英文）", example = "summaryEn", required = true)
    @NotBlank(message = "项目简介（英文）不能为空")
    @Size(max = 100, message = "项目简介（英文）必须为100位以内字符")
    private String summaryEn;

    @ApiModelProperty(position = 6, value = "项目详情（中文）", example = "项目详情内容", required = true)
    @NotBlank(message = "项目详情（中文）不能为空")
    private String descriptionZh;

    @ApiModelProperty(position = 7, value = "项目详情（英文）", example = "descriptionEn", required = true)
    @NotBlank(message = "项目详情（英文）不能为空")
    private String descriptionEn;

    @ApiModelProperty(position = 8, value = "募集截止时间", example = "2019-11-30 11:22:33", dataType = "java.util.Date", required = true)
    @NotNull(message = "募集截止时间不能为空")
    private Timestamp recruitmentEndTime;

    @ApiModelProperty(position = 9, value = "启动时间", example = "2019-11-30 11:22:33", dataType = "java.util.Date")
    private Timestamp startTime;

    @ApiModelProperty(position = 10, value = "预定总募集数额", example = "10000000", required = true)
    @NotNull(message = "预定总募集数额不能为空")
    @Positive(message = "预定总募集数额必须大于0")
    private BigDecimal totalAmount;

    @ApiModelProperty(position = 11, value = "发起人（中文）", example = "链猫科技", required = true)
    @NotBlank(message = "发起人（中文）不能为空")
    @Size(max = 50, message = "发起人（中文）必须为50位以内字符")
    private String initiatorZh;

    @ApiModelProperty(position = 12, value = "发起人（英文）", example = "initiatorEn", required = true)
    @NotBlank(message = "发起人（英文）不能为空")
    @Size(max = 50, message = "发起人（英文）必须为50位以内字符")
    private String initiatorEn;

    @ApiModelProperty(position = 13, value = "发起人质押数额", example = "500000", required = true)
    @NotNull(message = "发起人质押数额不能为空")
    @Positive(message = "发起人质押数额必须大于0")
    private BigDecimal initiatorPayAmount;

    @ApiModelProperty(position = 14, value = "锁仓天数", example = "180", required = true)
    @NotNull(message = "锁仓天数不能为空")
    @Positive(message = "锁仓天数必须大于0")
    private Integer lockDays;

    @ApiModelProperty(position = 20, value = "参与条件_人数上限", example = "200")
    @Positive(message = "参与条件_人数上限必须大于0")
    private Integer conditionMaxJoinNumber;

    @ApiModelProperty(position = 21, value = "参与条件_持仓数额", example = "2000")
    @Positive(message = "参与条件_持仓数额必须大于0")
    private BigDecimal conditionMinLockedAmount;

    @ApiModelProperty(position = 22, value = "参与条件_最低投资数额", example = "1000")
    @Positive(message = "参与条件_最低投资数额必须大于0")
    private BigDecimal conditionMinPayAmount;

    @ApiModelProperty(position = 23, value = "参与条件_最少注册天数", example = "60")
    @Positive(message = "参与条件_最少注册天数必须大于0")
    private Integer conditionMinRegisterDays;
}