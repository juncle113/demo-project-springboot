package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 钱包信息
 *
 * @author sunli
 * @date 2019/02/14
 */
@ApiModel(value = "WalletDTO", description = "钱包信息")
@Data
public class WalletDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "币种", example = "BTC", required = true)
    @NotBlank(message = "币种不能为空")
    @Pattern(regexp = "\\b(BTC|ETH)\\b", message = "币种错误")
    private String coinKind;

    @ApiModelProperty(position = 2, value = "钱包地址", example = "sdssfsdg34234sdfsdfw12", required = true)
    @NotBlank(message = "钱包地址不能为空")
    @Size(max = 100, message = "钱包地址必须为100位以内字符")
    private String address;

    @ApiModelProperty(position = 3, value = "地址名称", example = "提币BTC", required = true)
    @NotBlank(message = "地址名称不能为空")
    @Size(max = 20, message = "地址名称必须为20位以内字符")
    private String name;

    @ApiModelProperty(position = 4, value = "备注", example = "备注内容")
    @Size(max = 200, message = "备注必须为200位以内字符")
    private String memo;
}