package com.cpto.dapp.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账户记录信息
 *
 * @author sunli
 * @date 2019/01/27
 */
@ApiModel(value = "AccountLogVO", description = "账户记录信息")
@Data
public class AccountLogVO extends BaseVO {

    @ApiModelProperty(position = 1, value = "转账类型（1：充值，2：提币申请，3：提币申请-手续费，4：提币失败，5：提币失败-手续费，6：投资项目，7：退出投资，8：释放锁定，9：项目收益，10：大盘奖励）", example = "4")
    private Integer transferType;

    @ApiModelProperty(position = 2, value = "转账类型名称", example = "投资项目")
    private String transferTypeName;

    @ApiModelProperty(position = 3, value = "转账类型分组（1：支付，2：收益，3：充值，4：提币）", example = "1")
    private Integer transferTypeGroup;

    @ApiModelProperty(position = 4, value = "转账类型分组名称", example = "支付")
    private String transferTypeGroupName;

    @ApiModelProperty(position = 5, value = "转账说明", example = "转账说明内容")
    private String transferMemo;

    @ApiModelProperty(position = 6, value = "账户id", example = "154788255771735")
    private Long accountId;

    @ApiModelProperty(position = 7, value = "账户类型", example = "1")
    private Integer accountType;

    @ApiModelProperty(position = 8, value = "账户类型名称", example = "可用账户")
    private String accountTypeName;

    @ApiModelProperty(position = 9, value = "数额", example = "10000.000000")
    private BigDecimal amount;

    @ApiModelProperty(position = 10, value = "余额", example = "5000.000000")
    private BigDecimal balance;

    @ApiModelProperty(position = 11, value = "币种", example = "cpto")
    private String coinKind;

    @ApiModelProperty(position = 12, value = "关联id", example = "154788255771735")
    private Long relationId;

    @ApiModelProperty(position = 13, value = "关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）", example = "1")
    private Integer relationType;

    @ApiModelProperty(position = 14, value = "关联类型名称", example = "提币申请id")
    private String relationTypeName;
}