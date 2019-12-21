package com.cpto.dapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单信息
 *
 * @author sunli
 * @date 2019/01/16
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "order_info", comment = "订单信息")
public class OrderInfo extends Base {

    @Id
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", columnDefinition = "bigint unsigned comment '邀请人id'")
    private User inviter;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", columnDefinition = "bigint unsigned comment '项目id'", nullable = false)
    private Project project;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '支付数额'", nullable = false)
    private BigDecimal payAmount;

    @Column(columnDefinition = "timestamp null comment '锁仓释放时间'")
    private Timestamp lockEndTime;
}