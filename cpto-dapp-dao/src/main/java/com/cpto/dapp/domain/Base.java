package com.cpto.dapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Base
 *
 * @author sunli
 * @date 2018/12/27
 */
@Data
@MappedSuperclass
public class Base implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "int unsigned comment '状态（1：有效，2：无效）'", nullable = false)
    private Integer status;

    @Column(columnDefinition = "varchar(200) comment '管理备注'")
    private String remark;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", columnDefinition = "bigint unsigned comment '创建人id'")
    private ManagerAdmin createdBy;

    @Column(columnDefinition = "timestamp null comment '创建时间'")
    private Timestamp createdTime;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by_id", columnDefinition = "bigint unsigned comment '修改人id'")
    private ManagerAdmin modifiedBy;

    @Column(columnDefinition = "timestamp null comment '修改时间'")
    private Timestamp modifiedTime;
}