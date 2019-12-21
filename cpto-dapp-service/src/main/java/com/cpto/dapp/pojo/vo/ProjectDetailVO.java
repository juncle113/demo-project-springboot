package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 项目详细信息
 *
 * @author sunli
 * @date 2019/01/19
 */
@ApiModel(value = "ProjectDetailVO", description = "项目详细信息")
@Data
public class ProjectDetailVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目编号", example = "20190401")
    private String no;

    @ApiModelProperty(position = 2, value = "项目名称", example = "特斯拉油田")
    private String name;

    @ApiModelProperty(position = 3, value = "项目名称（中文）", example = "项目名称")
    private String nameZh;

    @ApiModelProperty(position = 4, value = "项目名称（英文）", example = "nameEn")
    private String nameEn;

    @ApiModelProperty(position = 5, value = "项目简介", example = "项目简介内容")
    private String summary;

    @ApiModelProperty(position = 6, value = "项目简介（中文）", example = "项目简介内容")
    private String summaryZh;

    @ApiModelProperty(position = 7, value = "项目简介（英文）", example = "summaryEn")
    private String summaryEn;

    @ApiModelProperty(position = 8, value = "项目详情", example = "项目详情内容")
    private String description;

    @ApiModelProperty(position = 9, value = "项目详情（中文）", example = "项目详情内容")
    private String descriptionZh;

    @ApiModelProperty(position = 10, value = "项目详情（英文）", example = "descriptionEn")
    private String descriptionEn;

    @ApiModelProperty(position = 11, value = "发起人", example = "链猫科技")
    private String initiator;

    @ApiModelProperty(position = 12, value = "发起人（中文）", example = "链猫科技")
    private String initiatorZh;

    @ApiModelProperty(position = 13, value = "发起人（英文）", example = "initiatorEn")
    private String initiatorEn;

    @ApiModelProperty(position = 14, value = "参与人数", example = "50")
    private Integer joinNumber;

    @ApiModelProperty(position = 15, value = "募集截止时间", example = "2019-01-15 20:00:00", dataType = "java.util.Date")
    private Timestamp recruitmentEndTime;

    @ApiModelProperty(position = 16, value = "启动时间", example = "2019-01-15 20:00:00", dataType = "java.util.Date")
    private Timestamp startTime;

    @ApiModelProperty(position = 17, value = "预定总募集数额", example = "10000000")
    private BigDecimal totalAmount;

    @ApiModelProperty(position = 18, value = "发起人质押数额", example = "10000000")
    private BigDecimal initiatorPayAmount;

    @ApiModelProperty(position = 19, value = "已募集数额", example = "5000000")
    private BigDecimal payAmountSum;

    @ApiModelProperty(position = 20, value = "募集进度", example = "0.46")
    private BigDecimal progress;

    @ApiModelProperty(position = 21, value = "锁仓天数", example = "180")
    private Integer lockDays;

    @ApiModelProperty(position = 22, value = "参与条件_人数上限", example = "200")
    private Integer conditionMaxJoinNumber;

    @ApiModelProperty(position = 23, value = "参与条件_持仓数额", example = "2000")
    private BigDecimal conditionMinLockedAmount;

    @ApiModelProperty(position = 24, value = "参与条件_最低投资数额", example = "1000")
    private BigDecimal conditionMinPayAmount;

    @ApiModelProperty(position = 25, value = "参与条件_最少注册天数", example = "60")
    private Integer conditionMinRegisterDays;

    @ApiModelProperty(position = 26, value = "资源信息")
    private List<SourceVO> sources;

    @ApiModelProperty(position = 27, value = "可继续投资", example = "true")
    private Boolean payable;

    @ApiModelProperty(position = 28, value = "可邀请投资", example = "true")
    private Boolean inviteable;

    @ApiModelProperty(position = 29, value = "成功", example = "false")
    private Boolean succeed;

    @ApiModelProperty(position = 30, value = "失败", example = "false")
    private Boolean failed;
}