package com.cpto.dapp.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 个人收益记录
 *
 * @author sunli
 * @date 2019/02/12
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "log_user_income", comment = "个人收益记录")
public class LogUserIncome extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_project_income_id", columnDefinition = "bigint unsigned comment '项目收益记录id'", nullable = false)
    private LogProjectIncome logProjectIncome;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", columnDefinition = "bigint unsigned comment '项目id'", nullable = false)
    private Project project;

    @Column(columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private Long userId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", columnDefinition = "bigint unsigned comment '订单id'", nullable = false)
    private OrderInfo orderInfo;

    @Column(columnDefinition = "decimal(18, 6) comment '数额'", nullable = false)
    private BigDecimal amount;
}