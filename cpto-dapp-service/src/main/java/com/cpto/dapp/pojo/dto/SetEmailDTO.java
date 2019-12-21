package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 绑定邮箱信息
 *
 * @author sunli
 * @date 2019/01/15
 */
@ApiModel(value = "SetEmailDTO", description = "绑定邮箱信息")
@Data
public class SetEmailDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "邮箱", example = "xyz@dapp.com", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Size(max = 50, message = "邮箱必须为50个字符以内")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(position = 2, value = "验证码", example = "222222", required = true)
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须为6个字符")
    @Pattern(regexp = "^[0-9]*$", message = "验证码必须为数字")
    private String code;
}