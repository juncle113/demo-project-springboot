package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 版本信息
 *
 * @author sunli
 * @date 2019/01/07
 */
@ApiModel(value = "VersionVO", description = "版本信息")
@Data
public class VersionVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "设备类型", example = "1")
    private Integer deviceType;

    @ApiModelProperty(position = 2, value = "设备类型名称", example = "android")
    private String deviceTypeName;

    @ApiModelProperty(position = 3, value = "最新版本号名称", example = "1.0.10")
    private String newVersion;

    @ApiModelProperty(position = 4, value = "最新版本号名称", example = "1.0.5")
    private String minVersion;

    @ApiModelProperty(position = 5, value = "更新说明", example = "修复Bug")
    private String description;

    @ApiModelProperty(position = 6, value = "是否有新更新", example = "true")
    private Boolean hasUpdate;

    @ApiModelProperty(position = 7, value = "是否强制更新", example = "false")
    private Boolean forceUpdate;

    @ApiModelProperty(position = 8, value = "发布更新时间", example = "2019-01-02 09:45:00", dataType = "java.util.Date")
    private Timestamp updateTime;
}