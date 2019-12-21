package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 项目评估信息
 *
 * @author sunli
 * @date 2019/02/02
 */
@ApiModel(value = "ProjectEvaluationDTO", description = "项目评估信息")
@Data
public class ProjectEvaluationDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "S1预期增值（%）", example = "10")
    @PositiveOrZero(message = "预期增值S1（%）不能小于0")
    private BigDecimal s1ExpectRange;

    @ApiModelProperty(position = 2, value = "S2预期增值（%）", example = "20")
    @PositiveOrZero(message = "预期增值S2（%）不能小于0")
    private BigDecimal s2ExpectRange;

    @ApiModelProperty(position = 3, value = "S3预期增值（%）", example = "30")
    @PositiveOrZero(message = "预期增值S3（%）不能小于0")
    private BigDecimal s3ExpectRange;

    @ApiModelProperty(position = 4, value = "S1预计回报（中文）", example = "S1预计回报（中文）")
    private String s1ExpectReturnZh;

    @ApiModelProperty(position = 5, value = "S1预计回报（英文）", example = "s1ExpectReturnEn")
    private String s1ExpectReturnEn;

    @ApiModelProperty(position = 6, value = "S2预计回报（中文）", example = "S2预计回报（中文）")
    private String s2ExpectReturnZh;

    @ApiModelProperty(position = 7, value = "S2预计回报（英文）", example = "s2ExpectReturnEn")
    private String s2ExpectReturnEn;

    @ApiModelProperty(position = 8, value = "S3预计回报（中文）", example = "S3预计回报（中文）")
    private String s3ExpectReturnZh;

    @ApiModelProperty(position = 9, value = "S3预计回报（英文）", example = "s3ExpectReturnEn")
    private String s3ExpectReturnEn;

    @ApiModelProperty(position = 10, value = "S1评估值（中文）", example = "100万美元")
    @Size(max = 50, message = "S1评估值（中文）必须为50以内字符")
    private String s1Zh;

    @ApiModelProperty(position = 11, value = "S1评估值（英文）", example = "s1En")
    @Size(max = 50, message = "S1评估值（英文）必须为50以内字符")
    private String s1En;

    @ApiModelProperty(position = 12, value = "S1评估人（中文）", example = "S1评估人（中文）")
    @Size(max = 50, message = "S1评估人（中文）必须为50以内字符")
    private String s1EvaluatorZh;

    @ApiModelProperty(position = 13, value = "S1评估人（英文）", example = "s1EvaluatorEn")
    @Size(max = 50, message = "S1评估人（英文）必须为50以内字符")
    private String s1EvaluatorEn;

    @ApiModelProperty(position = 14, value = "S1回报（中文）", example = "S1回报（中文）")
    private String s1ReturnZh;

    @ApiModelProperty(position = 15, value = "S1回报（英文）", example = "s1ReturnEn")
    private String s1ReturnEn;

    @ApiModelProperty(position = 16, value = "S2评估值（中文）", example = "200万美元")
    @Size(max = 50, message = "S2评估值（中文）必须为50以内字符")
    private String s2Zh;

    @ApiModelProperty(position = 17, value = "S2评估值（英文）", example = "s2En")
    @Size(max = 50, message = "S2评估值（英文）必须为50以内字符")
    private String s2En;

    @ApiModelProperty(position = 18, value = "S2评估人（中文）", example = "S2评估人（中文）")
    @Size(max = 50, message = "S2评估人（中文）必须为50以内字符")
    private String s2EvaluatorZh;

    @ApiModelProperty(position = 19, value = "S2评估人（英文）", example = "s2EvaluatorEn")
    @Size(max = 50, message = "S2评估人（英文）必须为50以内字符")
    private String s2EvaluatorEn;

    @ApiModelProperty(position = 20, value = "S2回报（中文）", example = "S2回报（中文）")
    private String s2ReturnZh;

    @ApiModelProperty(position = 21, value = "S2回报（英文）", example = "s2ReturnEn")
    private String s2ReturnEn;

    @ApiModelProperty(position = 22, value = "S3评估值（中文）", example = "300万美元")
    @Size(max = 50, message = "S3评估值（中文）必须为50以内字符")
    private String s3Zh;

    @ApiModelProperty(position = 23, value = "S3评估值（英文）", example = "s3En")
    @Size(max = 50, message = "S3评估值（英文）必须为50以内字符")
    private String s3En;

    @ApiModelProperty(position = 24, value = "S3评估人（中文）", example = "S3评估人（中文）")
    @Size(max = 50, message = "S3评估人（中文）必须为50以内字符")
    private String s3EvaluatorZh;

    @ApiModelProperty(position = 25, value = "S3评估人（英文）", example = "s3EvaluatorEn")
    @Size(max = 50, message = "S3评估人（英文）必须为50以内字符")
    private String s3EvaluatorEn;

    @ApiModelProperty(position = 26, value = "S3回报（中文）", example = "S3回报（中文）")
    private String s3ReturnZh;

    @ApiModelProperty(position = 27, value = "S3回报（英文）", example = "s3ReturnEn")
    private String s3ReturnEn;
}