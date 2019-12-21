package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 重置支付密码信息
 *
 * @author sunli
 * @date 2019/01/10
 */
@ApiModel(value = "ResetPayPasswordDTO", description = "重置支付密码信息")
@Data
public class ResetPayPasswordDTO extends ModifyPayPasswordDTO {

    @ApiModelProperty(position = 1, value = "手机号归属地代码", example = "86")
    @Size(max = 4, message = "手机号归属地代码必须为4个字符以内")
    @Pattern(regexp = "^[0-9]*$", message = "手机号归属地代码必须为数字")
    private String areaCode;

    @ApiModelProperty(position = 2, value = "手机号", example = "13712345678")
    @Size(max = 32, message = "手机号必须为32个字符以内")
    @Pattern(regexp = "^[0-9]*$", message = "手机号必须为数字")
    private String phone;

    @ApiModelProperty(position = 3, value = "邮箱", example = "xyz@dapp.com")
    @Size(max = 50, message = "邮箱必须为50个字符以内")
    @Email(message = "邮箱格式错误")
    private String email;
}