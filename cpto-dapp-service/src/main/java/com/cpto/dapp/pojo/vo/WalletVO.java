package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 钱包信息
 *
 * @author sunli
 * @date 2019/02/14
 */
@ApiModel(value = "WalletVO", description = "钱包信息")
@Data
public class WalletVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "用户id", example = "154639528842762")
    private Long userId;

    @ApiModelProperty(position = 2, value = "钱包类型（1：内部充值地址，2：外部提币地址）", example = "154639528842762")
    private Integer walletType;

    @ApiModelProperty(position = 3, value = "钱包类型名称", example = "外部提币地址")
    private String walletTypeName;

    @ApiModelProperty(position = 4, value = "币种", example = "BTC")
    private String coinKind;

    @ApiModelProperty(position = 5, value = "钱包地址", example = "asdavbwr123sdghsvaw2423fsd")
    private String address;

    @ApiModelProperty(position = 6, value = "地址名称", example = "提币BTC")
    private String name;

    @ApiModelProperty(position = 7, value = "备注", example = "备注内容")
    private String memo;
}