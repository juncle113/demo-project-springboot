package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 系统设置信息
 *
 * @author sunli
 * @date 2019/01/31
 */
@ApiModel(value = "SystemSettingsDTO", description = "系统设置信息")
@Data
public class SystemSettingsDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "参数值", example = "0.1", required = true)
    @NotBlank(message = "参数值不能为空")
    @Size(max = 2000, message = "参数值必须为2000位以内字符")
    private String paramValue;
}