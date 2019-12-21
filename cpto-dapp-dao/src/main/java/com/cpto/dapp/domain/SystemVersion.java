package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * 版本
 *
 * @author sunli
 * @date 2019/01/07
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "system_version", comment = "版本")
public class SystemVersion extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '设备类型（1：android，2：ios）'", nullable = false)
    private Integer deviceType;

    @Column(columnDefinition = "varchar(20) comment '最新版本号名称'", nullable = false)
    private String newVersion;

    @Column(columnDefinition = "varchar(20) comment '支持最小版本号名称'", nullable = false)
    private String minVersion;

    @Column(columnDefinition = "varchar(500) comment '版本说明（中文）'")
    private String descriptionZh;

    @Column(columnDefinition = "varchar(500) comment '版本说明（英文）'")
    private String descriptionEn;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '是否强制更新（0：不强制，1：强制）'", nullable = false)
    private Boolean forceUpdate;
}
