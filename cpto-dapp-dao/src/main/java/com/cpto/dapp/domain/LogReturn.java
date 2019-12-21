package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 释放记录
 *
 * @author sunli
 * @date 2019/01/15
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "log_return", comment = "释放记录")
public class LogReturn extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '记录类型（1：充值，2：提币，3：提币手续费，4：返还本金，5：收益）'", nullable = false)
    private Integer logType;

    @Column(columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private String userId;

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
