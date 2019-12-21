package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 项目收益信息
 *
 * @author sunli
 * @date 2019/02/03
 */
@ApiModel(value = "ProjectIncomeDTO", description = "项目收益信息")
@Data
public class ProjectIncomeDTO extends ManagerBaseDTO {

    @ApiModelProperty(position = 1, value = "发放阶段（4：S1完成，6：S2完成，8：S3完成）", example = "4", required = true)
    @NotNull(message = "发放阶段不能为空")
    private Integer projectStatus;

    @ApiModelProperty(position = 2, value = "数额", example = "1000000", required = true)
    @NotNull(message = "数额不能为空")
    @Positive(message = "数额必须大于0")
    private BigDecimal amount;
}