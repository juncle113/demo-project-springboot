package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * 系统公告
 *
 * @author sunli
 * @date 2019/01/15
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "system_notice", comment = "系统公告")
public class SystemNotice extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(100) comment '内容（中文）'", nullable = false)
    private String contentZh;

    @Column(columnDefinition = "varchar(100) comment '内容（英文）'", nullable = false)
    private String contentEn;
}