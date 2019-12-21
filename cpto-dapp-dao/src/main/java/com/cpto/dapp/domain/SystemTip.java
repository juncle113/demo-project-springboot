package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * 提示信息
 *
 * @author sunli
 * @date 2019/01/23
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "system_tip", comment = "提示信息")
public class SystemTip extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(50) unique comment '标题'", nullable = false)
    private String title;

    @Column(columnDefinition = "varchar(50) unique comment '内容（中文）'", nullable = false)
    private String contentZh;

    @Column(columnDefinition = "varchar(50) unique comment '内容（英文）'", nullable = false)
    private String contentEn;

}
