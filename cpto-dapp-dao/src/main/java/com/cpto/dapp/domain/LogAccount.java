package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 账户记录
 *
 * @author sunli
 * @date 2019/01/15
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "log_account", comment = "账户记录")
public class LogAccount extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '转账类型（1：充值，2：提币申请，3：提币申请-手续费，4：提币失败，5：提币失败-手续费，6：投资项目，7：退出投资，8：释放锁定，9：项目收益，10：大盘奖励）'", nullable = false)
    private Integer transferType;

    @Column(columnDefinition = "varchar(50) comment '转账说明（中文）'", nullable = false)
    private String transferMemoZh;

    @Column(columnDefinition = "varchar(50) comment '转账说明（英文）'", nullable = false)
    private String transferMemoEn;

    @Column(columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private Long userId;

    @Column(columnDefinition = "bigint unsigned comment '账户id'", nullable = false)
    private Long accountId;

    @Column(columnDefinition = "int unsigned comment '账户类型（1：可用账户，2：锁定账户，3：待审核账户）'", nullable = false)
    private Integer accountType;

    @Column(columnDefinition = "decimal(18, 6) comment '数额'", nullable = false)
    private BigDecimal amount;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '余额'", nullable = false)
    private BigDecimal balance;

    @Column(columnDefinition = "bigint unsigned comment '关联id'")
    private Long relationId;

    @Column(columnDefinition = "int unsigned comment '关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）'")
    private Integer relationType;
}