package com.cpto.dapp.domain;


import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 项目评估信息
 *
 * @author sunli
 * @date 2019/02/02
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "project_evaluation", comment = "项目评估信息")
@SQLDelete(sql = "update project_evaluation set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class ProjectEvaluation extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned unique comment 'project_id'", nullable = false)
    private Long projectId;

    @Column(columnDefinition = "decimal(18, 2) unsigned comment 'S1预期增值（%）'")
    private BigDecimal s1ExpectRange;

    @Column(columnDefinition = "decimal(18, 2) unsigned comment 'S2预期增值（%）'")
    private BigDecimal s2ExpectRange;

    @Column(columnDefinition = "decimal(18, 2) unsigned comment 'S3预期增值（%）'")
    private BigDecimal s3ExpectRange;

    @Column(columnDefinition = "text comment 'S1预计回报（中文）'")
    private String s1ExpectReturnZh;

    @Column(columnDefinition = "text comment 'S1预计回报（英文）'")
    private String s1ExpectReturnEn;

    @Column(columnDefinition = "text comment 'S2预计回报（中文）'")
    private String s2ExpectReturnZh;

    @Column(columnDefinition = "text comment 'S2预计回报（英文）'")
    private String s2ExpectReturnEn;

    @Column(columnDefinition = "text comment 'S3预计回报（中文）'")
    private String s3ExpectReturnZh;

    @Column(columnDefinition = "text comment 'S3预计回报（英文）'")
    private String s3ExpectReturnEn;

    @Column(columnDefinition = "text comment 'S1回报（中文）'")
    private String s1ReturnZh;

    @Column(columnDefinition = "text comment 'S1回报（英文）'")
    private String s1ReturnEn;

    @Column(columnDefinition = "text comment 'S1评估值（中文）'")
    private String s1Zh;

    @Column(columnDefinition = "text comment 'S1评估值（英文）'")
    private String s1En;

    @Column(columnDefinition = "varchar(50)  comment 'S1评估人（中文）'")
    private String s1EvaluatorZh;

    @Column(columnDefinition = "varchar(50) comment 'S1评估人（英文）'")
    private String s1EvaluatorEn;

    @Column(columnDefinition = "text comment 'S2评估值（中文）'")
    private String s2Zh;

    @Column(columnDefinition = "text comment 'S2评估值（英文）'")
    private String s2En;

    @Column(columnDefinition = "varchar(50)  comment 'S2评估人（中文）'")
    private String s2EvaluatorZh;

    @Column(columnDefinition = "varchar(50)  comment 'S2评估人（英文）'")
    private String s2EvaluatorEn;

    @Column(columnDefinition = "text comment 'S2回报（中文）'")
    private String s2ReturnZh;

    @Column(columnDefinition = "text comment 'S2回报（英文）'")
    private String s2ReturnEn;

    @Column(columnDefinition = "text comment 'S3评估值（中文）'")
    private String s3Zh;

    @Column(columnDefinition = "text comment 'S3评估值（英文）'")
    private String s3En;

    @Column(columnDefinition = "varchar(50)  comment 'S3评估人（中文）'")
    private String s3EvaluatorZh;

    @Column(columnDefinition = "varchar(50)  comment 'S3评估人（英文）'")
    private String s3EvaluatorEn;

    @Column(columnDefinition = "text comment 'S3回报（中文）'")
    private String s3ReturnZh;

    @Column(columnDefinition = "text comment 'S3回报（英文）'")
    private String s3ReturnEn;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}