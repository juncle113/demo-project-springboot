package com.cpto.dapp.domain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 公告
 * 删除处理为逻辑删除
 * 查询处理只取得未被删除的记录
 *
 * @author sunli
 * @date 2018/12/27
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "notice", comment = "公告")
@SQLDelete(sql = "update notice set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class Notice extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '公告类型（1：项目公告，2：回报公告）'", nullable = false)
    private Integer noticeType;

    @Column(columnDefinition = "varchar(50) comment '标题（中文）'", nullable = false)
    private String titleZh;

    @Column(columnDefinition = "varchar(50) comment '标题（英文）'", nullable = false)
    private String titleEn;

    @Column(columnDefinition = "varchar(20) comment '编号'")
    private String no;

    @Column(columnDefinition = "varchar(50) comment '作者（中文）'")
    private String authorZh;

    @Column(columnDefinition = "varchar(50) comment '作者（英文）'")
    private String authorEn;

    @Column(columnDefinition = "varchar(2000) comment '网址（中文）'")
    private String urlZh;

    @Column(columnDefinition = "varchar(2000) comment '网址（英文）'")
    private String urlEn;

    @Column(columnDefinition = "text comment '内容（中文）'")
    private String contentZh;

    @Column(columnDefinition = "text comment '内容（英文）'")
    private String contentEn;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}