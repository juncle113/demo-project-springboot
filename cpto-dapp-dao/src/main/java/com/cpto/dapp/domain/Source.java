package com.cpto.dapp.domain;


import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 资源
 *
 * @author sunli
 * @date 2019/01/31
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "source", comment = "资源")
@SQLDelete(sql = "update source set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class Source extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned comment '业务资源id'", nullable = false)
    private Long relationId;

    @Column(columnDefinition = "int unsigned comment '关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）'")
    private Integer relationType;

    @Column(columnDefinition = "varchar(50) comment '资源名称'", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(2000) comment '资源地址'", nullable = false)
    private String url;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}