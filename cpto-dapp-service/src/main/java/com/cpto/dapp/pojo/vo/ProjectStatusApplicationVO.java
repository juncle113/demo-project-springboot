package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目状态申请信息
 *
 * @author sunli
 * @date 2019/03/12
 */
@ApiModel(value = "ProjectStatusApplicationVO", description = "项目状态申请信息")
@Data
public class ProjectStatusApplicationVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "id", example = "11")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "项目编号", example = "20190401")
    private String projectNo;

    @ApiModelProperty(position = 3, value = "项目名称（中文）", example = "特斯拉油田01")
    private String projectNameZh;

    @ApiModelProperty(position = 4, value = "项目名称（英文）", example = "projectNameEn")
    private String projectNameEn;

    @ApiModelProperty(position = 5, value = "审批前状态", example = "2")
    private Integer fromStatus;

    @ApiModelProperty(position = 6, value = "审批前状态名称", example = "开始募集")
    private String fromStatusName;

    @ApiModelProperty(position = 7, value = "审批后状态", example = "3")
    private Integer toStatus;

    @ApiModelProperty(position = 8, value = "审批后状态名称", example = "募集成功")
    private String toStatusName;

    @ApiModelProperty(position = 9, value = "说明", example = "募集数额满足要求")
    private String memo;
}