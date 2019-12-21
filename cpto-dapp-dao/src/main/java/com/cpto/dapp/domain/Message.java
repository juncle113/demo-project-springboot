package com.cpto.dapp.domain;


import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 消息
 *
 * @author sunli
 * @date 2019/02/18
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "message", comment = "消息")
@SQLDelete(sql = "update message set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class Message extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '消息类型（1：系统消息，2：投资消息）'", nullable = false)
    private Integer messageType;

    @Column(columnDefinition = "varchar(50) comment '内容（中文）'", nullable = false)
    private String titleZh;

    @Column(columnDefinition = "varchar(50) comment '内容（英文）'", nullable = false)
    private String titleEn;

    @Column(columnDefinition = "varchar(100) comment '内容（中文）'", nullable = false)
    private String contentZh;

    @Column(columnDefinition = "varchar(100) comment '内容（英文）'", nullable = false)
    private String contentEn;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}