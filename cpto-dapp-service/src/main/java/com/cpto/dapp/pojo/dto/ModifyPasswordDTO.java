package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 修改登录密码信息
 *
 * @author sunli
 * @date 2019/01/04
 */
@ApiModel(value = "ModifyPasswordDTO", description = "修改登录密码信息")
@Data
public class ModifyPasswordDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "验证方式（1：手机短信，2：电子邮件）", example = "1", required = true)
    @NotNull(message = "验证方式不能为空")
    @Range(min = 1, max = 2, message = "验证方式错误")
    private Integer verifyType;

    @ApiModelProperty(position = 2, value = "验证码", example = "222222", required = true)
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须为6个字符")
    @Pattern(regexp = "^[0-9]*$", message = "验证码必须为数字")
    private String code;

    @ApiModelProperty(position = 3, value = "密码", example = "88888888", required = true)
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8,16}$", message = "密码必须为8-16位字母或数字组合")
    private String password;
}