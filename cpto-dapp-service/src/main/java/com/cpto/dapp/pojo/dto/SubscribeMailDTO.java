package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 订阅邮件信息
 *
 * @author sunli
 * @date 2019/02/15
 */
@ApiModel(value = "SubscribeMailDTO", description = "订阅邮件信息")
@Data
public class SubscribeMailDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "是否订阅邮件", example = "true", required = true)
    @NotNull(message = "是否订阅邮件不能为空")
    private Boolean isSubscribeMail;
}