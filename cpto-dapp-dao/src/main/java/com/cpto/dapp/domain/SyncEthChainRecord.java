package com.cpto.dapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 同步ETH区块转账记录
 *
 * @author sunli
 * @date 2019/04/11
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "sync_eth_chain_record", comment = "同步ETH区块转账记录")
public class SyncEthChainRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned comment '用户id'")
    private Long userId;

    @Column(columnDefinition = "varchar(100) comment '同步用地址'")
    private String syncAddress;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '是否已读（0：未读，1：已读）'")
    private Boolean isRead;

    @Column(columnDefinition = "timestamp null comment '同步时间'")
    private Timestamp syncTime;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String blockNumber;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String timeStamp;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String hash;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String nonce;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String blockHash;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String transactionIndex;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String fromAddress;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String toAddress;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String ethValue;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String gas;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String gasPrice;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String isError;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String txReceiptStatus;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String input;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String contractAddress;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String cumulativeGasUsed;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String gasUsed;

    @Column(columnDefinition = "varchar(100) comment ''")
    private String confirmations;
}