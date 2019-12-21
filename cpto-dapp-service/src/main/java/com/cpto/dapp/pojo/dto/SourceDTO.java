package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 资源信息
 *
 * @author sunli
 * @date 2019/01/31
 */
@ApiModel(value = "SourceDTO", description = "资源信息")
@Data
public class SourceDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "关联id", example = "11", required = true)
    @NotNull(message = "关联id不能为空")
    private Long relationId;

    @ApiModelProperty(position = 2, value = "关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）", example = "4", required = true)
    @NotNull(message = "关联类型不能为空")
    @Range(min = 1, max = 5, message = "关联类型错误")
    private Integer relationType;

    @ApiModelProperty(position = 3, value = "资源名称", example = "项目图片01.jpg", required = true)
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 50, message = "资源名称必须为500位以内字符")
    private String name;

    @ApiModelProperty(position = 4, value = "资源地址", example = "http://fanyi.youdao.com/?keyfrom=fanyi-new.logo", required = true)
    @NotBlank(message = "资源地址不能为空")
    @Size(max = 2000, message = "资源地址必须为2000位以内字符")
    private String url;
}