package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账户资产信息
 *
 * @author sunli
 * @date 2019/01/27
 */
@ApiModel(value = "AccountAssetsVO", description = "账户资产信息")
@Data
public class AccountAssetsVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "总资产（可用资产 + 锁定资产）", example = "10000.000000")
    private BigDecimal totalAmount;

    @ApiModelProperty(position = 2, value = "可用资产", example = "3000.000000")
    private BigDecimal availableAmount;

    @ApiModelProperty(position = 3, value = "锁定资产", example = "7000.000000")
    private BigDecimal lockedAmount;

    @ApiModelProperty(position = 4, value = "待审核资产", example = "2000.000000")
    private BigDecimal approvedAmount;

    @ApiModelProperty(position = 5, value = "累计收益", example = "1000.000000")
    private BigDecimal incomeAmountSum;
}