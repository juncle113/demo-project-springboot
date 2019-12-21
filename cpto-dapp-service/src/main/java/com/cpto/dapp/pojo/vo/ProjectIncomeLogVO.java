package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目收益记录信息
 *
 * @author sunli
 * @date 2019/03/06
 */
@ApiModel(value = "ProjectIncomeLogVO", description = "项目收益记录信息")
@Data
public class ProjectIncomeLogVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目id", example = "11")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "发放时项目状态（4：S1结束，6：S2结束，8：S3结束）", example = "4")
    private Integer projectStatus;

    @ApiModelProperty(position = 3, value = "发放时项目状态名称", example = "S1结束")
    private String projectStatusName;

    @ApiModelProperty(position = 4, value = "数额", example = "100000")
    private BigDecimal amount;

    @ApiModelProperty(position = 5, value = "备注（中文）", example = "备注（中文）")
    private String memoZh;

    @ApiModelProperty(position = 6, value = "备注（英文）", example = "memoEn")
    private String memoEn;

    @ApiModelProperty(position = 7, value = "备注2（中文）", example = "备注2（中文）")
    private String memo2Zh;

    @ApiModelProperty(position = 8, value = "备注2（英文）", example = "memo2En")
    private String memo2En;
}