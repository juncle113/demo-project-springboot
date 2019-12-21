package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 项目报告信息
 *
 * @author sunli
 * @date 2019/03/18
 */
@ApiModel(value = "ProjectReportDTO", description = "项目报告信息")
@Data
public class ProjectReportDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "标题（中文）", example = "标题（中文）", required = true)
    @NotBlank(message = "标题（中文）不能为空")
    @Size(max = 50, message = "标题（中文）必须为50位以内字符")
    private String titleZh;

    @ApiModelProperty(position = 2, value = "标题（英文）", example = "titleEn", required = true)
    @NotBlank(message = "标题（英文）不能为空")
    @Size(max = 50, message = "标题（英文）必须为50位以内字符")
    private String titleEn;

    @ApiModelProperty(position = 3, value = "回报公式（中文）", example = "回报公式（中文）", required = true)
    @NotBlank(message = "回报公式（中文）不能为空")
    @Size(max = 50, message = "回报公式（中文）必须为50位以内字符")
    private String returnMemoZh;

    @ApiModelProperty(position = 4, value = "回报公式（英文）", example = "returnMemoEn", required = true)
    @NotBlank(message = "回报公式（英文）不能为空")
    @Size(max = 50, message = "回报公式（英文）必须为50位以内字符")
    private String returnMemoEn;

    @ApiModelProperty(position = 5, value = "报告月份", example = "2019-03", required = true)
    @NotBlank(message = "报告月份不能为空")
    @Size(max = 20, message = "报告月份必须为20位以内字符")
    private String x1Month;

    @ApiModelProperty(position = 6, value = "美元价值", example = "1000000", required = true)
    @NotNull(message = "美元价值不能为空")
    @Positive(message = "美元价值必须大于0")
    private BigDecimal y1USD;

    @ApiModelProperty(position = 7, value = "CPTO数额", example = "1000000", required = true)
    @NotNull(message = "CPTO数额不能为空")
    @Positive(message = "CPTO数额必须大于0")
    private BigDecimal y2CPTO;
}