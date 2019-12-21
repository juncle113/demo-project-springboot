package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Url地址信息
 *
 * @author sunli
 * @date 2019/02/25
 */
@ApiModel(value = "UrlInfoVO", description = "Url地址信息")
@Data
public class UrlInfoVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "广告图片地址", example = "http://fanyi.youdao.com/?keyfrom=fanyi-new.logo")
    private String adPicUrl;

    @ApiModelProperty(position = 2, value = "广告页跳转网址", example = "http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png")
    private String adUrl;

    @ApiModelProperty(position = 3, value = "app下载网址", example = "https://pgyer.com/bmVy")
    private String appDownloadUrl;

    @ApiModelProperty(position = 4, value = "帮助网址", example = "https://web-static-dapp.cpto.io/help.html?lang=zh")
    private String helpUrl;

    @ApiModelProperty(position = 5, value = "用户协议网址", example = "https://web-static-dapp.cpto.io/userAgreement.html?lang=zh")
    private String userAgreementUrl;
}