package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目报告详细信息
 *
 * @author sunli
 * @date 2019/02/28
 */
@ApiModel(value = "ProjectReportDetailVO", description = "项目报告详细信息")
@Data
public class ProjectReportDetailVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目id", example = "22")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "标题", example = "首次产油实际为2019年1月6日")
    private String title;

    @ApiModelProperty(position = 3, value = "标题（中文）", example = "首次产油实际为2019年1月6日")
    private String titleZh;

    @ApiModelProperty(position = 4, value = "标题（英文）", example = "titleEn")
    private String titleEn;

    @ApiModelProperty(position = 5, value = "回报公式", example = "当月纯利润50%")
    private String returnMemo;

    @ApiModelProperty(position = 6, value = "回报公式（中文）", example = "当月纯利润50%")
    private String returnMemoZh;

    @ApiModelProperty(position = 7, value = "回报公式（英文）", example = "returnMemoEn")
    private String returnMemoEn;

    @ApiModelProperty(position = 8, value = "横轴-月份", example = "2019-01")
    private String x1Month;

    @ApiModelProperty(position = 9, value = "纵轴1-美元", example = "1000000")
    private BigDecimal y1USD;

    @ApiModelProperty(position = 10, value = "纵轴2-CPTO数量", example = "1000000")
    private BigDecimal y2CPTO;
}