package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 反馈信息
 *
 * @author sunli
 * @date 2019/03/04
 */
@ApiModel(value = "FeedbackVO", description = "反馈信息")
@Data
public class FeedbackVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "内容", example = "内容")
    private String content;

    @ApiModelProperty(position = 2, value = "反馈类型（1：申请，2：投诉）", example = "1")
    private Integer feedbackType;

    @ApiModelProperty(position = 3, value = "反馈类型名称", example = "申请")
    private String feedbackTypeName;

    @ApiModelProperty(position = 4, value = "用户id", example = "1")
    private Long userId;

    @ApiModelProperty(position = 5, value = "用户名", example = "user001")
    private String userName;
}