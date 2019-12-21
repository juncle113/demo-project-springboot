package com.cpto.dapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 验证码记录
 *
 * @author sunli
 * @date 2018/12/31
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "log_code", comment = "验证码记录")
public class LogCode extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '验证方式（1：手机短信，2：电子邮件）'", nullable = false)
    private Integer verifyType;

    @Column(columnDefinition = "varchar(50) comment '发送地址'")
    private String sendTo;

    @Column(columnDefinition = "int unsigned comment '验证场合（1：注册，2：修改密码，3：修改支付密码，4：绑定手机号，5：绑定邮箱）'")
    private Integer verifyCase;

    @Column(columnDefinition = "char(6) comment '验证码'")
    private String code;

    @Column(columnDefinition = "timestamp null comment '过期时间'")
    private Timestamp expiresTime;

}
