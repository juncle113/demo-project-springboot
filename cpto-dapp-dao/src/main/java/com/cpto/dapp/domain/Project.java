package com.cpto.dapp.domain;


import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 项目
 *
 * @author sunli
 * @date 2019/01/10
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "project", comment = "项目")
@SQLDelete(sql = "update project set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class Project extends Base {

    @Id
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(20) comment '项目编号'", nullable = false)
    private String no;

    @Column(columnDefinition = "varchar(50) comment '项目名称（中文）'", nullable = false)
    private String nameZh;

    @Column(columnDefinition = "varchar(50) comment '项目名称（英文）'", nullable = false)
    private String nameEn;

    @Column(columnDefinition = "varchar(100) comment '项目简介（中文）'", nullable = false)
    private String summaryZh;

    @Column(columnDefinition = "varchar(100) comment '项目简介（英文）'", nullable = false)
    private String summaryEn;

    @Column(columnDefinition = "text comment '项目详情（中文）'", nullable = false)
    private String descriptionZh;

    @Column(columnDefinition = "text comment '项目详情（英文）'", nullable = false)
    private String descriptionEn;

    @Column(columnDefinition = "timestamp null comment '募集截止时间'")
    private Timestamp recruitmentEndTime;

    @Column(columnDefinition = "timestamp null comment '启动时间'")
    private Timestamp startTime;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '预定总募集数额'", nullable = false)
    private BigDecimal totalAmount;

    @Column(columnDefinition = "varchar(50) comment '发起人（中文）'", nullable = false)
    private String initiatorZh;

    @Column(columnDefinition = "varchar(50) comment '发起人（英文）'", nullable = false)
    private String initiatorEn;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '发起人质押数额'", nullable = false)
    private BigDecimal initiatorPayAmount;

    @Column(columnDefinition = "int unsigned comment '锁仓天数'", nullable = false)
    private Integer lockDays;

    @Column(columnDefinition = "int unsigned comment '参与条件_人数上限'")
    private Integer conditionMaxJoinNumber;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '参与条件_持仓数额'")
    private BigDecimal conditionMinLockedAmount;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '参与条件_最低投资数额'")
    private BigDecimal conditionMinPayAmount;

    @Column(columnDefinition = "int unsigned comment '参与条件_最少注册天数'")
    private Integer conditionMinRegisterDays;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}