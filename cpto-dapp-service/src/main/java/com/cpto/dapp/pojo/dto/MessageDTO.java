package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 消息信息
 *
 * @author sunli
 * @date 2019/02/18
 */
@ApiModel(value = "MessageDTO", description = "消息信息")
@Data
public class MessageDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "标题（中文）", example = "系统消息", required = true)
    @NotBlank(message = "标题（中文）不能为空")
    @Size(max = 50, message = "标题（中文）必须为50个字符以内")
    private String titleZh;

    @ApiModelProperty(position = 2, value = "标题（英文）", example = "titleEn", required = true)
    @NotBlank(message = "标题（英文）不能为空")
    @Size(max = 50, message = "标题（英文）必须为50个字符以内")
    private String titleEn;

    @ApiModelProperty(position = 3, value = "内容（中文）", example = "系统消息", required = true)
    @NotBlank(message = "内容（中文）不能为空")
    @Size(max = 100, message = "内容（中文）必须为100个字符以内")
    private String contentZh;

    @ApiModelProperty(position = 4, value = "内容（英文）", example = "系统消息", required = true)
    @NotBlank(message = "内容（英文）不能为空")
    @Size(max = 100, message = "内容（英文）必须为100个字符以内")
    private String contentEn;

    @ApiModelProperty(position = 5, value = "消息类型（1：系统消息，2：投资消息）", example = "1")
    @NotNull(groups = {Add.class}, message = "消息类型不能为空")
    @Range(min = 1, max = 2, message = "消息类型错误")
    private Integer messageType;
}