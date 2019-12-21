package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 钱包信息
 *
 * @author sunli
 * @date 2019/01/15
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "wallet", comment = "钱包信息")
public class Wallet extends Base {

    @Id
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private Long userId;

    @Column(columnDefinition = "int unsigned comment '钱包地址类型（1：充值，2：提币）'", nullable = false)
    private Integer addressType;

    @Column(columnDefinition = "varchar(10) comment '币种'", nullable = false)
    private String coinKind;

    @Column(columnDefinition = "varchar(100) comment '钱包地址'", nullable = false)
    private String address;

    @Column(columnDefinition = "varchar(20) comment '地址名称'", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(200) comment '备注'")
    private String memo;
}