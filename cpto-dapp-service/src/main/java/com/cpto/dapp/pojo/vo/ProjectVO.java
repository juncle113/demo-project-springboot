package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 项目信息
 *
 * @author sunli
 * @date 2019/01/26
 */
@ApiModel(value = "ProjectVO", description = "项目信息")
@Data
public class ProjectVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目编号", example = "20190401")
    private String no;

    @ApiModelProperty(position = 2, value = "项目名称", example = "特斯拉油田")
    private String name;

    @ApiModelProperty(position = 3, value = "项目名称（中文）", example = "项目名称（中文）")
    private String nameZh;

    @ApiModelProperty(position = 4, value = "项目名称（英文）", example = "nameEn")
    private String nameEn;

    @ApiModelProperty(position = 5, value = "发起人", example = "链猫科技")
    private String initiator;

    @ApiModelProperty(position = 6, value = "发起人（中文）", example = "发起人（中文）")
    private String initiatorZh;

    @ApiModelProperty(position = 7, value = "发起人（英文）", example = "initiatorEn")
    private String initiatorEn;

    @ApiModelProperty(position = 8, value = "募集截止时间", example = "2019-01-15 20:00:00", dataType = "java.util.Date")
    private Timestamp recruitmentEndTime;

    @ApiModelProperty(position = 9, value = "启动时间", example = "2019-01-15 20:00:00", dataType = "java.util.Date")
    private Timestamp startTime;

    @ApiModelProperty(position = 10, value = "预定总募集数额", example = "10000000")
    private BigDecimal totalAmount;

    @ApiModelProperty(position = 11, value = "锁仓天数", example = "180")
    private Integer lockDays;

    @ApiModelProperty(position = 12, value = "已募集数额", example = "500000")
    private BigDecimal payAmountSum;

    @ApiModelProperty(position = 13, value = "募集进度", example = "0.5")
    private BigDecimal progress;

    @ApiModelProperty(position = 14, value = "订单数量", example = "180")
    private Integer orderCount;
}