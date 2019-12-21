package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 项目概要信息
 *
 * @author sunli
 * @date 2019/01/13
 */
@ApiModel(value = "ProjectSummaryVO", description = "项目概要信息")
@Data
public class ProjectSummaryVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目名称", example = "特斯拉油田")
    private String name;

    @ApiModelProperty(position = 2, value = "发起人", example = "链猫科技")
    private String initiator;

    @ApiModelProperty(position = 3, value = "参与人数", example = "50")
    private Integer joinNumber;

    @ApiModelProperty(position = 4, value = "锁仓天数", example = "180")
    private Integer lockDays;

    @ApiModelProperty(position = 5, value = "预定总募集数额", example = "10000000")
    private BigDecimal totalAmount;

    @ApiModelProperty(position = 6, value = "募集进度", example = "0.46")
    private BigDecimal progress;

    @ApiModelProperty(position = 7, value = "参与条件_人数上限", example = "200")
    private Integer conditionMaxJoinNumber;

    @ApiModelProperty(position = 8, value = "参与条件_持仓数额", example = "2000")
    private BigDecimal conditionMinLockedAmount;

    @ApiModelProperty(position = 9, value = "参与条件_最低投资数额", example = "1000")
    private BigDecimal conditionMinPayAmount;

    @ApiModelProperty(position = 10, value = "参与条件_最少注册天数", example = "60")
    private Integer conditionMinRegisterDays;

    @ApiModelProperty(position = 11, value = "募集截止时间", example = "2019-01-15 20:00:00", dataType = "java.util.Date")
    private Timestamp recruitmentEndTime;
}