package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 价值总量
 *
 * @author sunli
 * @date 2019/01/10
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "system_total_value", comment = "价值总量")
public class SystemTotalValue extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '设置类型（1：价值概要，2：总量分配，3：流通来源）'", nullable = false)
    private Integer valueType;

    @Column(columnDefinition = "int comment '序号'")
    private Integer sortNo;

    @Column(columnDefinition = "varchar(50) comment '标题（中文）'", nullable = false)
    private String titleZh;

    @Column(columnDefinition = "varchar(50) comment '标题（英文）'", nullable = false)
    private String titleEn;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '数额'")
    private BigDecimal amount;

    @Column(columnDefinition = "varchar(20) comment '颜色'")
    private String color;
}