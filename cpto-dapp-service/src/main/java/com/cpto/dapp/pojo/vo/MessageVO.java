package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息信息
 *
 * @author sunli
 * @date 2019/02/18
 */
@ApiModel(value = "MessageVO", description = "消息信息")
@Data
public class MessageVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "标题", example = "系统消息")
    private String title;

    @ApiModelProperty(position = 2, value = "标题（中文）", example = "系统消息")
    private String titleZh;

    @ApiModelProperty(position = 3, value = "标题（英文）", example = "titleEn")
    private String titleEn;

    @ApiModelProperty(position = 4, value = "内容", example = "新的版本上线了")
    private String content;

    @ApiModelProperty(position = 5, value = "内容（中文）", example = "新的版本上线了")
    private String contentZh;

    @ApiModelProperty(position = 6, value = "内容（英文）", example = "contentEn")
    private String contentEn;

    @ApiModelProperty(position = 7, value = "消息类型（1：系统消息，2：投资消息）", example = "1")
    private Integer messageType;

    @ApiModelProperty(position = 8, value = "消息类型名称", example = "系统消息")
    private String messageTypeName;
}