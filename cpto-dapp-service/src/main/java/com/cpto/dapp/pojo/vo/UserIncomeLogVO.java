package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户收益记录信息
 *
 * @author sunli
 * @date 2019/02/22
 */
@ApiModel(value = "UserIncomeLogVO", description = "用户收益记录信息")
@Data
public class UserIncomeLogVO {

    @ApiModelProperty(position = 1, value = "用户收益项目记录信息")
    private List<UserIncomeProjectLogVO> details;

    @ApiModelProperty(position = 2, value = "合计", example = "10000")
    private BigDecimal totalAmount;
}