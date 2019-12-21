package com.cpto.dapp.domain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 管理员
 * 删除处理为逻辑删除
 * 查询处理只取得未被删除的记录
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "manager_admin", comment = "管理员")
@SQLDelete(sql = "update manager_admin set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class ManagerAdmin extends Base {

    @Id
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(16) unique comment '用户名'", nullable = false)
    private String userName;

    @Column(columnDefinition = "varchar(32) comment '密码'", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(16) comment '姓名'", nullable = false)
    private String name;

    @Column(columnDefinition = "int unsigned comment '角色类型（1：系统管理员，2：超级管理员，3：普通管理员）'", nullable = false)
    private Integer roleType;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '删除标记（0：未删除，1：已删除）'", nullable = false)
    private Boolean deleted;
}
