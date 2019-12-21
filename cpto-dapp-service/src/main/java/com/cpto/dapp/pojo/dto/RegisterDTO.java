package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * 注册信息
 *
 * @author sunli
 * @date 2018/12/31
 */
@ApiModel(value = "RegisterDTO", description = "注册信息")
@Data
public class RegisterDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "验证方式（1：手机短信，2：电子邮件）", example = "1", required = true)
    @NotNull(message = "验证方式不能为空")
    @Range(min = 1, max = 2, message = "验证方式错误")
    private Integer verifyType;

    @ApiModelProperty(position = 2, value = "手机号归属地代码", example = "86")
    @Size(max = 4, message = "手机号归属地代码必须为4个字符以内")
    @Pattern(regexp = "^[0-9]*$", message = "手机号归属地代码必须为数字")
    private String areaCode;

    @ApiModelProperty(position = 3, value = "手机号", example = "13712345678")
    @Size(max = 32, message = "手机号必须为32个字符以内")
    @Pattern(regexp = "^[0-9]*$", message = "手机号必须为数字")
    private String phone;

    @ApiModelProperty(position = 4, value = "邮箱地址", example = "xyz@dapp.com")
    @Size(max = 50, message = "邮箱地址必须为50个字符以内")
    @Email(message = "邮箱地址格式错误")
    private String email;

    @ApiModelProperty(position = 5, value = "验证码", example = "222222", required = true)
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须为6个字符")
    @Pattern(regexp = "^[0-9]*$", message = "验证码必须为数字")
    private String code;

    @ApiModelProperty(position = 6, value = "用户名", example = "user001", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 16, message = "用户名必须为2-16个字符")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "用户名必须为字母或数字")
    private String userName;

    @ApiModelProperty(position = 7, value = "密码", example = "a1111111", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 16, message = "密码应该为8-16个字符")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "密码必须为字母或数字")
    private String password;

    @ApiModelProperty(position = 8, value = "邀请码", example = "ABCD1234")
    @Size(min = 8, max = 8, message = "邀请码必须为8个字符")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "邀请码必须为字母或数字")
    private String inviteCode;
}