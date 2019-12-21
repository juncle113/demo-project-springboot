package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 项目状态申请信息
 *
 * @author sunli
 * @date 2019/03/12
 */
@ApiModel(value = "ProjectStatusApplicationDTO", description = "项目状态申请信息")
@Data
public class ProjectStatusApplicationDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "项目id", example = "11", required = true)
    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "审批前状态", example = "2", required = true)
    @NotNull(message = "审批前状态不能为空")
    private Integer fromStatus;

    @ApiModelProperty(position = 3, value = "审批后状态", example = "3", required = true)
    @NotNull(message = "审批后状态不能为空")
    private Integer toStatus;

    @ApiModelProperty(position = 4, value = "说明", example = "募集数额满足要求", required = true)
    @NotBlank(message = "说明不能为空")
    private String memo;
}