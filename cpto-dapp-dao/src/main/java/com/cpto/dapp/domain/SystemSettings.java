package com.cpto.dapp.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 系统设置
 *
 * @author sunli
 * @date 2019/02/21
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "system_settings", comment = "系统设置")
public class SystemSettings extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(100) comment '参数名称'", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(50) unique comment '参数键'", nullable = false)
    private String paramKey;

    @Column(columnDefinition = "varchar(500) comment '参数值'", nullable = false)
    private String paramValue;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '后台管理可否设置'", nullable = false)
    private Boolean editable;

    @Column(columnDefinition = "bigint unsigned comment '最小值限制'")
    private Long minLimit;

    @Column(columnDefinition = "bigint unsigned comment '最大值限制'")
    private Long maxLimit;
}