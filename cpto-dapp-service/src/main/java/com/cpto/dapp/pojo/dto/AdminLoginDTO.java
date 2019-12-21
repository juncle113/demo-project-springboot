package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 管理员登录信息
 *
 * @author sunli
 * @date 2018/12/07
 */
@ApiModel(value = "AdminLoginDTO", description = "管理员登录信息")
@Data
public class AdminLoginDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "用户名", example = "root", required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(position = 2, value = "密码", example = "88888888", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}