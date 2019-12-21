package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提币申请信息
 *
 * @author sunli
 * @date 2019/04/14
 */
@ApiModel(value = "WithdrawalApplicationVO", description = "提币申请信息")
@Data
public class WithdrawalApplicationVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "用户id", example = "1")
    private Long userId;

    @ApiModelProperty(position = 2, value = "用户名", example = "user001")
    private String userName;

    @ApiModelProperty(position = 3, value = "兑换币种", example = "BTC")
    private String toCoinKind;

    @ApiModelProperty(position = 4, value = "转出地址", example = "petroleum")
    private String fromChainAddress;

    @ApiModelProperty(position = 5, value = "转入地址", example = "fw23ljanf24sfawekr23j4234")
    private String toChainAddress;

    @ApiModelProperty(position = 6, value = "区块链备注", example = "区块链备注")
    private String chainMemo;

    @ApiModelProperty(position = 7, value = "兑换汇率", example = "0.00001")
    private BigDecimal rate;

    @ApiModelProperty(position = 8, value = "提币数额", example = "100000")
    private BigDecimal cptoAmount;

    @ApiModelProperty(position = 9, value = "兑换数额", example = "10")
    private BigDecimal toAmount;

    @ApiModelProperty(position = 10, value = "兑换手续费率", example = "0.05")
    private BigDecimal feeRate;

    @ApiModelProperty(position = 11, value = "兑换手续费", example = "0.000001")
    private BigDecimal fee;

    @ApiModelProperty(position = 12, value = "手续费币种", example = "BTC")
    private String feeCoinKind;

    @ApiModelProperty(position = 13, value = "cpto余额", example = "2000")
    private BigDecimal cptoBalance;
}