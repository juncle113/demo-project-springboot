package com.cpto.dapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 同步BTC区块转账记录
 *
 * @author sunli
 * @date 2019/04/11
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "sync_btc_chain_record", comment = "同步BTC区块转账记录")
public class SyncBtcChainRecord implements Serializable {

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

    @Column(columnDefinition = "varchar(100) comment '账号'")
    private String account;

    @Column(columnDefinition = "varchar(100) comment '地址'")
    private String address;

    @Column(columnDefinition = "varchar(100) comment '转账类型'")
    private String category;

    @Column(columnDefinition = "varchar(100) comment '数额'")
    private String amount;

    @Column(columnDefinition = "varchar(100) comment '链交易记录标签'")
    private String label;

    @Column(columnDefinition = "varchar(100) comment 'vOut'")
    private String vOut;

    @Column(columnDefinition = "varchar(100) comment '确认数'")
    private String confirmations;

    @Column(columnDefinition = "varchar(100) comment '区块Hash'")
    private String blockHash;

    @Column(columnDefinition = "varchar(100) comment '区块Index'")
    private String blockIndex;

    @Column(columnDefinition = "varchar(100) comment '区块时间'")
    private String blockTime;

    @Column(columnDefinition = "varchar(100) comment '交易id'")
    private String txId;

    @Column(columnDefinition = "text comment '钱包冲突'")
    private String walletConflicts;

    @Column(columnDefinition = "varchar(100) comment '交易时间'")
    private String time;

    @Column(columnDefinition = "varchar(100) comment '接收时间'")
    private String timeReceived;

    @Column(columnDefinition = "varchar(100) comment 'bip125Replaceable'")
    private String bip125Replaceable;
}

