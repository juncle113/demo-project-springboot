package com.cpto.dapp.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 转账信息
 *
 * @author sunli
 * @date 2019/01/17
 */
@ApiModel(value = "TransferAccountDTO", description = "转账信息")
@Data
public class TransferAccountDTO extends BaseDTO {

    @ApiModelProperty(position = 1, value = "转账类型（1：充值，2：提币申请，3：提币申请-手续费，4：提币失败，5：提币失败-手续费，6：投资项目，7：退出投资，8：释放锁定，9：项目收益，10：大盘奖励）", example = "6", required = true)
    @NotNull(message = "转账类型不能为空")
    @Range(min = 1, max = 10, message = "转账类型错误")
    private Integer transferType;

    @ApiModelProperty(position = 2, value = "转账说明（中文）", example = "投资项目")
    private String transferMemoZh;

    @ApiModelProperty(position = 3, value = "转账说明（英文）", example = "transferMemoEn")
    private String transferMemoEn;

    @ApiModelProperty(position = 4, value = "出账用户id", example = "11", required = true)
    @NotNull(message = "出账用户id不能为空")
    private Long outUserId;

    @ApiModelProperty(position = 5, value = "出账账户类型（1：可用账户，2：锁定账户，3：待审核账户）", example = "1", required = true)
    @NotBlank(message = "出账账户类型不能为空")
    private Integer outAccountType;

    @ApiModelProperty(position = 6, value = "入账用户id", example = "11", required = true)
    @NotNull(message = "入账用户id不能为空")
    private Long inUserId;

    @ApiModelProperty(position = 7, value = "入账账户类型（1：可用账户，2：锁定账户，3：待审核账户）", example = "2", required = true)
    @NotNull(message = "入账账户类型不能为空")
    private Integer inAccountType;

    @ApiModelProperty(position = 8, value = "转账数额", example = "1000", required = true)
    @NotNull(message = "转账数额不能为空")
    @Positive(message = "转账数额必须大于0")
    private BigDecimal amount;

    @ApiModelProperty(position = 9, value = "关联id", example = "1", required = true)
    @NotNull(message = "关联id不能为空")
    private Long relationId;

    @ApiModelProperty(position = 10, value = "关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）", example = "2", required = true)
    @NotNull(message = "关联类型不能为空")
    @Range(min = 1, max = 5, message = "关联类型错误")
    private Integer relationType;
}