package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 登录信息
 *
 * @author sunli
 * @date 2018/12/29
 */
@ApiModel(value = "LoginDTO", description = "登录信息")
@Data
@Validated
@Valid
public class LoginDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "登录账号（用户名/手机号/邮箱）", example = "testsunli01", required = true)
    @NotBlank(message = "登录账号不能为空")
    private String account;

    @ApiModelProperty(position = 2, value = "密码", example = "88888888", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}