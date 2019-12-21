package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 绑定手机号信息
 *
 * @author sunli
 * @date 2019/01/15
 */
@ApiModel(value = "SetPhoneDTO", description = "绑定手机号信息")
@Data
public class SetPhoneDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "手机号归属地代码", example = "86", required = true)
    @NotBlank(message = "手机号归属地代码不能为空")
    @Size(max = 4, message = "手机号归属地代码必须为4个字符以内")
    @Pattern(regexp = "^[0-9]*$", message = "手机号归属地代码必须为数字")
    private String areaCode;

    @ApiModelProperty(position = 2, value = "手机号", example = "13712345678", required = true)
    @NotBlank(message = "手机号不能为空")
    @Size(max = 32, message = "手机号必须为32个字符以内")
    @Pattern(regexp = "^[0-9]*$", message = "手机号必须为数字")
    private String phone;

    @ApiModelProperty(position = 3, value = "验证码", example = "222222", required = true)
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须为6个字符")
    @Pattern(regexp = "^[0-9]*$", message = "验证码必须为数字")
    private String code;
}