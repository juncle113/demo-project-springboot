package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 发送验证码信息
 *
 * @author sunli
 * @date 2019/01/03
 */
@ApiModel(value = "SendCodeDTO", description = "发送验证码信息")
@Data
public class SendCodeDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "验证方式（1：手机短信，2：电子邮件）", example = "1", required = true)
    @NotNull(message = "验证方式不能为空")
    @Range(min = 1, max = 2, message = "验证方式错误")
    private Integer verifyType;

    @ApiModelProperty(position = 2, value = "验证场合（1：注册，2：修改密码，3：修改支付密码，4：绑定手机号，5：绑定邮箱）", example = "1", required = true)
    @NotNull(message = "验证场合不能为空")
    @Range(min = 1, max = 5, message = "验证场合错误")
    private Integer verifyCase;

    @ApiModelProperty(position = 3, value = "手机号归属地代码（验证方式为1的场合必填）", example = "86")
    @Pattern(regexp = "^[0-9]*$", message = "手机号归属地代码必须为数字")
    private String areaCode;

    @ApiModelProperty(position = 4, value = "手机号（验证方式为1的场合必填）", example = "13752448393")
    @Pattern(regexp = "^[0-9]*$", message = "手机号必须为数字")
    private String phone;

    @ApiModelProperty(position = 5, value = "邮箱地址（验证方式为2的场合必填）", example = "xyz@dapp.com")
    @Email(message = "邮箱地址格式错误")
    private String email;
}