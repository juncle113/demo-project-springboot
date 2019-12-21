package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户收益明细记录信息
 *
 * @author sunli
 * @date 2019/06/11
 */
@ApiModel(value = "UserIncomeDetailLogVO", description = "用户收益明细记录信息")
@Data
public class UserIncomeDetailLogVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目收益记录id", example = "1")
    private Long projectIncomeLogId;

    @ApiModelProperty(position = 2, value = "收益数额", example = "1000.100406")
    private BigDecimal amount;
}