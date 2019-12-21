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
 * 管理员信息
 *
 * @author sunli
 * @date 2018/12/07
 */
@ApiModel(value = "AdminDTO", description = "管理员信息")
@Data
public class AdminDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "用户名", example = "admin", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{2,16}$", message = "用户名必须为2-16位字母或数字组合")
    private String userName;

    @ApiModelProperty(position = 2, value = "密码", example = "abcd1234", required = true)
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8,16}$", message = "密码必须为8-16位字母或数字组合")
    private String password;

    @ApiModelProperty(position = 3, value = "姓名", example = "李四", required = true)
    @Size(max = 16, message = "姓名必须为16位以内字符")
    private String name;

    @ApiModelProperty(position = 4, value = "角色类型（1：系统管理员，2：超级管理员，3：普通管理员）", example = "2", required = true)
    @NotNull(message = "角色类型不能为空")
    @Range(min = 1, max = 3, message = "角色类型错误")
    private Integer roleType;
}