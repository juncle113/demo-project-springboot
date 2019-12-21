package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 币种汇率信息
 *
 * @author sunli
 * @date 2019/03/07
 */
@ApiModel(value = "ExchangeRateDTO", description = "币种汇率信息")
@Data
public class ExchangeRateDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "币种汇率", example = "0.1", required = true)
    @NotNull(message = "币种汇率不能为空")
    private BigDecimal rate;
}