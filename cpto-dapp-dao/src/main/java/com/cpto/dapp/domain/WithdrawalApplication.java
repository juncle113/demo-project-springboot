package com.cpto.dapp.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 提币申请
 *
 * @author sunli
 * @date 2019/01/15
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "withdrawal_application", comment = "提币申请")
public class WithdrawalApplication extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private User user;

    @Column(columnDefinition = "varchar(10) comment '兑换币种'", nullable = false)
    private String toCoinKind;

    @Column(columnDefinition = "varchar(100) comment '转出链账号地址'", nullable = false)
    private String fromChainAddress;

    @Column(columnDefinition = "varchar(100) comment '转入链账号地址'", nullable = false)
    private String toChainAddress;

    @Column(columnDefinition = "varchar(100) comment '区块链备注'")
    private String chainMemo;

    @Column(columnDefinition = "decimal(24, 12) unsigned comment '汇率'", nullable = false)
    private BigDecimal rate;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment 'cpto数额'", nullable = false)
    private BigDecimal cptoAmount;

    @Column(columnDefinition = "decimal(24, 12) unsigned comment '兑换数额'", nullable = false)
    private BigDecimal toAmount;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '兑换手续费率'", nullable = false)
    private BigDecimal feeRate;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '兑换手续费'", nullable = false)
    private BigDecimal fee;

    @Column(columnDefinition = "varchar(10) comment '兑换手续费币种'", nullable = false)
    private String feeCoinKind;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment 'cpto余额'", nullable = false)
    private BigDecimal cptoBalance;
}