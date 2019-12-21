package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息
 *
 * @author sunli
 * @date 2018/12/07
 */
@ApiModel(value = "UserVO", description = "用户信息")
@Data
public class UserVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "用户名", example = "user001")
    private String userName;

    @ApiModelProperty(position = 2, value = "是否已设置支付密码", example = "true")
    private Boolean hasPayPassword;

    @ApiModelProperty(position = 3, value = "手机号归属地代码", example = "86")
    private String areaCode;

    @ApiModelProperty(position = 4, value = "手机号", example = "13712345678")
    private String phone;

    @ApiModelProperty(position = 5, value = "邮箱", example = "xyz@dapp.com")
    private String email;

    @ApiModelProperty(position = 6, value = "是否订阅邮件", example = "true")
    private Boolean isSubscribeMail;

    @ApiModelProperty(position = 7, value = "邀请码", example = "ABCD1234")
    private String inviteCode;

    @ApiModelProperty(position = 8, value = "上级邀请人id")
    private Long parentId;
}