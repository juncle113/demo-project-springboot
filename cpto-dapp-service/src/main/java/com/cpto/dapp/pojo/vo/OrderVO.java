package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单信息
 *
 * @author sunli
 * @date 2019/01/24
 */
@ApiModel(value = "OrderVO", description = "订单信息")
@Data
public class OrderVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "用户id", example = "154857511051590")
    private Long userId;

    @ApiModelProperty(position = 2, value = "用户名", example = "testsunli01")
    private String userName;

    @ApiModelProperty(position = 3, value = "项目id", example = "11")
    private Long projectId;

    @ApiModelProperty(position = 4, value = "项目编号", example = "20190120")
    private String projectNo;

    @ApiModelProperty(position = 5, value = "项目名称", example = "特斯拉油田01")
    private String projectName;

    @ApiModelProperty(position = 6, value = "项目名称（中文）", example = "特斯拉油田01")
    private String projectNameZh;

    @ApiModelProperty(position = 7, value = "项目名称（英文）", example = "projectNameEn")
    private String projectNameEn;

    @ApiModelProperty(position = 8, value = "支付数额", example = "10000.000000")
    private BigDecimal payAmount;

    @ApiModelProperty(position = 9, value = "锁仓天数", example = "180")
    private Integer lockDays;

    @ApiModelProperty(position = 10, value = "锁仓释放时间", example = "2019-04-02 09:45:00", dataType = "java.util.Date")
    private Timestamp lockEndTime;

    @ApiModelProperty(position = 11, value = "项目状态（1：准备阶段，2：开始募集，3：募集成功，4：募集失败，5：S1进行中，6：S1结束，7：S2进行中，8：S2结束，9：S3进行中，10：S3结束，11：投资成功，12：投资失败，13：无效）", example = "1")
    private Integer projectStatus;

    @ApiModelProperty(position = 12, value = "项目状态名称", example = "开始")
    private String projectStatusName;

    @ApiModelProperty(position = 13, value = "可查看项目", example = "true")
    private Boolean reviewable;

    @ApiModelProperty(position = 14, value = "可继续投资", example = "true")
    private Boolean payable;

    @ApiModelProperty(position = 15, value = "可邀请投资", example = "true")
    private Boolean inviteable;

    @ApiModelProperty(position = 16, value = "可退出投资", example = "false")
    private Boolean cancelable;
}