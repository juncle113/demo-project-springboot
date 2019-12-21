package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目评估信息
 *
 * @author sunli
 * @date 2019/01/13
 */
@ApiModel(value = "ProjectEvaluationVO", description = "项目评估信息")
@Data
public class ProjectEvaluationVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目id", example = "21")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "项目名称", example = "特斯拉油田")
    private String projectName;

    @ApiModelProperty(position = 3, value = "S1预期增值（%）", example = "10")
    private BigDecimal s1ExpectRange;

    @ApiModelProperty(position = 4, value = "S2预期增值（%）", example = "20")
    private BigDecimal s2ExpectRange;

    @ApiModelProperty(position = 5, value = "S3预期增值（%）", example = "30")
    private BigDecimal s3ExpectRange;

    @ApiModelProperty(position = 6, value = "S1预计回报", example = "S1预计回报")
    private String s1ExpectReturn;

    @ApiModelProperty(position = 7, value = "S1预计回报（中文）", example = "S1预计回报（中文）")
    private String s1ExpectReturnZh;

    @ApiModelProperty(position = 8, value = "S1预计回报（英文）", example = "s1ExpectReturnEn")
    private String s1ExpectReturnEn;

    @ApiModelProperty(position = 9, value = "S2预计回报", example = "S2预计回报")
    private String s2ExpectReturn;

    @ApiModelProperty(position = 10, value = "S2预计回报（中文）", example = "S2预计回报（中文）")
    private String s2ExpectReturnZh;

    @ApiModelProperty(position = 11, value = "S2预计回报（英文）", example = "s2ExpectReturnEn")
    private String s2ExpectReturnEn;

    @ApiModelProperty(position = 12, value = "S3预计回报", example = "S3预计回报")
    private String s3ExpectReturn;

    @ApiModelProperty(position = 13, value = "S3预计回报（中文）", example = "S3预计回报（中文）")
    private String s3ExpectReturnZh;

    @ApiModelProperty(position = 14, value = "S3预计回报（英文）", example = "s3ExpectReturnEn")
    private String s3ExpectReturnEn;

    @ApiModelProperty(position = 15, value = "S1评估值", example = "100万美元")
    private String s1;

    @ApiModelProperty(position = 16, value = "S1评估值（中文）", example = "100万美元")
    private String s1Zh;

    @ApiModelProperty(position = 17, value = "S1评估值（英文）", example = "s1En")
    private String s1En;

    @ApiModelProperty(position = 18, value = "S1评估人", example = "S1评估人")
    private String s1Evaluator;

    @ApiModelProperty(position = 19, value = "S1评估人（中文）", example = "S1评估人（中文）")
    private String s1EvaluatorZh;

    @ApiModelProperty(position = 20, value = "S1评估人（英文）", example = "s1EvaluatorEn")
    private String s1EvaluatorEn;

    @ApiModelProperty(position = 21, value = "S1回报", example = "S1回报")
    private String s1Return;

    @ApiModelProperty(position = 22, value = "S1回报（中文）", example = "S1回报（中文）")
    private String s1ReturnZh;

    @ApiModelProperty(position = 23, value = "S1回报（英文）", example = "s1ReturnEn")
    private String s1ReturnEn;

    @ApiModelProperty(position = 24, value = "S2评估值", example = "200万美元")
    private String s2;

    @ApiModelProperty(position = 25, value = "S2评估值（中文）", example = "200万美元")
    private String s2Zh;

    @ApiModelProperty(position = 26, value = "S2评估值（英文）", example = "s2En")
    private String s2En;

    @ApiModelProperty(position = 27, value = "S2评估人", example = "S2评估人")
    private String s2Evaluator;

    @ApiModelProperty(position = 28, value = "S2评估人（中文）", example = "S2评估人（中文）")
    private String s2EvaluatorZh;

    @ApiModelProperty(position = 29, value = "S2评估人（英文）", example = "s2EvaluatorEn")
    private String s2EvaluatorEn;

    @ApiModelProperty(position = 30, value = "S2回报", example = "S2回报")
    private String s2Return;

    @ApiModelProperty(position = 31, value = "S2回报（中文）", example = "S2回报（中文）")
    private String s2ReturnZh;

    @ApiModelProperty(position = 32, value = "S2回报（英文）", example = "s2ReturnEn")
    private String s2ReturnEn;

    @ApiModelProperty(position = 33, value = "S2评估值", example = "200万美元")
    private String s3;

    @ApiModelProperty(position = 34, value = "S2评估值（中文）", example = "200万美元")
    private String s3Zh;

    @ApiModelProperty(position = 35, value = "S2评估值（英文）", example = "s3En")
    private String s3En;

    @ApiModelProperty(position = 36, value = "S3评估人", example = "S3评估人")
    private String s3Evaluator;

    @ApiModelProperty(position = 37, value = "S3评估人（中文）", example = "S3评估人（中文）")
    private String s3EvaluatorZh;

    @ApiModelProperty(position = 38, value = "S3评估人（英文）", example = "s3EvaluatorEn")
    private String s3EvaluatorEn;

    @ApiModelProperty(position = 39, value = "S3回报", example = "S3回报")
    private String s3Return;

    @ApiModelProperty(position = 40, value = "S3回报（中文）", example = "S3回报（中文）")
    private String s3ReturnZh;

    @ApiModelProperty(position = 41, value = "S3回报（英文）", example = "s3ReturnEn")
    private String s3ReturnEn;
}