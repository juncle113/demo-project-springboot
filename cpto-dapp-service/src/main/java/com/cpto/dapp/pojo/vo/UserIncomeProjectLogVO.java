package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户收益项目记录信息
 *
 * @author sunli
 * @date 2019/02/22
 */
@ApiModel(value = "UserIncomeProjectLogVO", description = "用户收益项目记录信息")
@Data
public class UserIncomeProjectLogVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "项目id", example = "11")
    private Long projectId;

    @ApiModelProperty(position = 2, value = "项目编号", example = "20190401")
    private String projectNo;

    @ApiModelProperty(position = 3, value = "项目名称", example = "特斯拉油田")
    private String projectName;

    @ApiModelProperty(position = 4, value = "订单id", example = "1")
    private Long orderId;

    @ApiModelProperty(position = 5, value = "收益数额", example = "1000.100406")
    private BigDecimal amount;
}