package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录信息
 *
 * @author sunli
 * @date 2018/12/07
 */
@ApiModel(value = "AdminLoginVO", description = "登录信息")
@Data
public class AdminLoginVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "token", example = "NGYwN2FlMGMyOWU3NGUzZl8x")
    private String token;
}