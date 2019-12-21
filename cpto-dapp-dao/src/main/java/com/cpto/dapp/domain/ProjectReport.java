package com.cpto.dapp.domain;


import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 项目报告数据
 *
 * @author sunli
 * @date 2019/02/28
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "project_report", comment = "项目报告数据")
@SQLDelete(sql = "update project_report set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class ProjectReport extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned comment 'project_id'", nullable = false)
    private Long projectId;

    @Column(columnDefinition = "varchar(50) comment '标题（中文）'", nullable = false)
    private String titleZh;

    @Column(columnDefinition = "varchar(50) comment '标题（英文）'", nullable = false)
    private String titleEn;

    @Column(columnDefinition = "varchar(50) comment '回报公式（中文）'", nullable = false)
    private String returnMemoZh;

    @Column(columnDefinition = "varchar(50) comment '回报公式（英文）'", nullable = false)
    private String returnMemoEn;

    @Column(columnDefinition = "varchar(20) comment '横轴-报告月份'", nullable = false)
    private String x1Month;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '纵轴1-美元价值'", nullable = false)
    private BigDecimal y1USD;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '纵轴2-cpto数量'", nullable = false)
    private BigDecimal y2cpto;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}