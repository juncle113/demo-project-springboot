package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资源信息
 *
 * @author sunli
 * @date 2019/01/31
 */
@ApiModel(value = "SourceVO", description = "资源信息")
@Data
public class SourceVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "关联id", example = "11")
    private Long relationId;

    @ApiModelProperty(position = 2, value = "关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）", example = "4")
    private Integer relationType;

    @ApiModelProperty(position = 3, value = "关联类型名称", example = "项目id")
    private String relationTypeName;

    @ApiModelProperty(position = 4, value = "资源名称", example = "项目图片01.jpg")
    private String name;

    @ApiModelProperty(position = 5, value = "资源地址", example = "http://fanyi.youdao.com/?keyfrom=fanyi-new.logo")
    private String url;
}