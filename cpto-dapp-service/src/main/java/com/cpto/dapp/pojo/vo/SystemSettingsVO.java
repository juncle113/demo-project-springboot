package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统参数信息（后台管理）
 *
 * @author sunli
 * @date 2019/02/21
 */
@ApiModel(value = "SystemSettingsVO", description = "系统参数信息（后台管理）")
@Data
public class SystemSettingsVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "参数名称", example = "提币手续费")
    private String name;

    @ApiModelProperty(position = 2, value = "参数key", example = "withdraw_fee")
    private String paramKey;

    @ApiModelProperty(position = 3, value = "参数值", example = "0.1")
    private String paramValue;

    @ApiModelProperty(position = 4, value = "后台管理可否设置", example = "true")
    private Boolean editable;

    @ApiModelProperty(position = 5, value = "最小值限制", example = "0.01")
    private Long minLimit;

    @ApiModelProperty(position = 6, value = "最大值限制", example = "1")
    private Long maxLimit;
}