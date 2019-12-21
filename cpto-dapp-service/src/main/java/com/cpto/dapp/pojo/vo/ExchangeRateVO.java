package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 币种汇率信息
 *
 * @author sunli
 * @date 2019/03/05
 */
@ApiModel(value = "ExchangeRateVO", description = "币种汇率信息")
@Data
public class ExchangeRateVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "源币种", example = "cpto")
    private String fromCoinKind;

    @ApiModelProperty(position = 2, value = "目标币种", example = "USDT")
    private String toCoinKind;

    @ApiModelProperty(position = 3, value = "汇率", example = "0.1")
    private BigDecimal rate;
}