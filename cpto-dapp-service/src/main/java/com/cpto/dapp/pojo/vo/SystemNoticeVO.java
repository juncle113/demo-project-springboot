package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统公告信息
 *
 * @author sunli
 * @date 2019/01/10
 */
@ApiModel(value = "SystemNoticeVO", description = "系统公告信息")
@Data
public class SystemNoticeVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "内容", example = "公告内容")
    private String content;
}