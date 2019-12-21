package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户
 *
 * @author sunli
 * @date 2018/12/27
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "user", comment = "用户")
public class User extends Base {

    @Id
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(16) unique comment '用户名'", nullable = false)
    private String userName;

    @Column(columnDefinition = "char(32) comment '登录密码'", nullable = false)
    private String password;

    @Column(columnDefinition = "char(32) comment '支付密码'")
    private String payPassword;

    @Column(columnDefinition = "varchar(4) comment '手机号归属地代码'")
    private String areaCode;

    @Column(columnDefinition = "varchar(32) comment '手机号'")
    private String phone;

    @Column(columnDefinition = "varchar(50) unique comment '邮箱'")
    private String email;

    @Column(columnDefinition = "tinyint unsigned default 0 comment '是否订阅邮件（0：否，1：是）'")
    private Boolean isSubscribeMail;

    @Column(columnDefinition = "char(8) unique comment '邀请码'")
    private String inviteCode;

    @Column(columnDefinition = "bigint unsigned comment '上级邀请人id'")
    private Long parentId;
}