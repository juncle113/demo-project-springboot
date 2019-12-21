package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 反馈信息
 *
 * @author sunli
 * @date 2019/01/29
 */
@ApiModel(value = "FeedbackDTO", description = "反馈信息")
@Data
public class FeedbackDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "反馈类型（1：申请，2：投诉）", example = "2")
    @NotNull(groups = {Add.class}, message = "反馈类型不能为空")
    @Range(min = 1, max = 2, message = "反馈类型错误")
    private Integer feedbackType;

    @ApiModelProperty(position = 2, value = "反馈内容", example = "反馈内容")
    @NotBlank(groups = {Add.class}, message = "反馈内容不能为空")
    private String content;
}