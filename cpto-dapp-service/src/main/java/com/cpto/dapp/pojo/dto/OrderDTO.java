package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 订单信息
 *
 * @author sunli
 * @date 2019/01/16
 */
@ApiModel(value = "OrderDTO", description = "订单信息")
@Data
public class OrderDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "项目id", example = "11", required = true)
    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "支付数额", example = "1000", required = true)
    @NotNull(message = "支付数额不能为空")
    @Positive(message = "支付数额必须大于0")
    private BigDecimal payAmount;

    @ApiModelProperty(position = 3, value = "邀请码", example = "ABCD1234")
    @Size(min = 8, max = 8, message = "邀请码必须为8个字符")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "邀请码必须为字母或数字")
    private String inviteCode;
}