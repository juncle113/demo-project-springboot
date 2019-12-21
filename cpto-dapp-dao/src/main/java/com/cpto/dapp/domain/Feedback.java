package com.cpto.dapp.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

/**
 * 反馈
 *
 * @author sunli
 * @date 2019/01/29
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "feedback", comment = "反馈")
public class Feedback extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "int unsigned comment '反馈类型'", nullable = false)
    private Integer feedbackType;

    @Column(columnDefinition = "varchar(500) comment '反馈内容'", nullable = false)
    private String content;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "bigint unsigned comment '用户id'")
    private User user;
}