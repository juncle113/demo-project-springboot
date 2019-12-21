package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请信息
 *
 * @author sunli
 * @date 2019/01/29
 */
@ApiModel(value = "InviteVO", description = "邀请信息")
@Data
public class InviteVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "邀请码", example = "ABCD1234")
    private String inviteCode;

    @ApiModelProperty(position = 2, value = "邀请次数", example = "10")
    private Integer inviteTimes;

    @ApiModelProperty(position = 3, value = "剩余奖励次数", example = "10")
    private Integer rewardTimes;

    @ApiModelProperty(position = 4, value = "app下载页面地址", example = "https://www.pgyer.com/ukkl")
    private String downloadUrl;

    @ApiModelProperty(position = 5, value = "背景图片地址", example = "https://www.pgyer.com/ukkl/background.png")
    private String backgroundUrl;
}