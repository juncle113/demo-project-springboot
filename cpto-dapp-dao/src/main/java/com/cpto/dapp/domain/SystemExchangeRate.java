package com.cpto.dapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 币种汇率
 *
 * @author sunli
 * @date 2019/03/05
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "system_exchange_rate", comment = "币种汇率")
public class SystemExchangeRate extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(10) comment '源币种'", nullable = false)
    private String fromCoinKind;

    @Column(columnDefinition = "varchar(10) comment '目标币种'", nullable = false)
    private String toCoinKind;

    @Column(columnDefinition = "decimal(24, 12) unsigned comment '汇率'", nullable = false)
    private BigDecimal rate;
}