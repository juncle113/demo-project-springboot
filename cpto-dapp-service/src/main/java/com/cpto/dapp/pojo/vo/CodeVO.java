package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 验证码信息
 *
 * @author sunli
 * @date 2019/01/03
 */
@ApiModel(value = "CodeVO", description = "验证码信息")
@Data
public class CodeVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "验证方式（1：手机短信，2：电子邮件）", example = "1")
    private Integer verifyType;

    @ApiModelProperty(position = 2, value = "验证方式名", example = "手机短信")
    private String verifyTypeName;

    @ApiModelProperty(position = 3, value = "发送地址", example = "8613712345678")
    private String sendTo;

    @ApiModelProperty(position = 4, value = "验证场合（1：注册，2：修改密码，3：修改支付密码，4：绑定手机号，5：绑定邮箱）", example = "1")
    private Integer verifyCase;

    @ApiModelProperty(position = 5, value = "验证场合名", example = "注册")
    private String verifyCaseName;

    @ApiModelProperty(position = 6, value = "超时时间（分钟）", example = "10")
    private Integer timeout;

    @ApiModelProperty(position = 7, value = "过期时间", example = "2019-01-02 09:50:00", dataType = "java.util.Date")
    private Timestamp expiresTime;
}