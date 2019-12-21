package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 提币申请信息
 *
 * @author sunli
 * @date 2019/04/14
 */
@ApiModel(value = "WithdrawalApplicationDTO", description = "提币申请信息")
@Data
public class WithdrawalApplicationDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "兑换币种", example = "BTC", required = true)
    @NotBlank(message = "兑换币种不能为空")
    @Pattern(regexp = "\\b(BTC|ETH)\\b", message = "兑换币种错误")
    private String toCoinKind;

    @ApiModelProperty(position = 2, value = "提币地址", example = "sdssfsdg34234sdfsdfw12", required = true)
    @NotBlank(message = "提币地址不能为空")
    @Size(max = 100, message = "提币地址必须为100位以内字符")
    private String toChainAddress;

    @ApiModelProperty(position = 3, value = "提币数额", example = "1000.5", required = true)
    @NotNull(message = "提币数额不能为空")
    @Positive(message = "提币数额必须大于0")
    private BigDecimal cptoAmount;

    @ApiModelProperty(position = 4, value = "备注", example = "备注内容")
    @Size(max = 100, message = "备注必须为100位以内字符")
    private String chainMemo;
}