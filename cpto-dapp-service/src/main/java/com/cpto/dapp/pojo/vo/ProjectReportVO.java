package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 项目报告信息
 *
 * @author sunli
 * @date 2019/02/28
 */
@ApiModel(value = "ProjectReportVO", description = "项目报告信息")
@Data
public class ProjectReportVO extends com.cpto.dapp.pojo.vo.BaseVO {

    @ApiModelProperty(position = 1, value = "项目id", example = "21")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "项目名称", example = "特斯拉油田")
    private String projectName;

    @ApiModelProperty(position = 3, value = "项目报告详细信息")
    private List<com.cpto.dapp.pojo.vo.ProjectReportDetailVO> details;
}