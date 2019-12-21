package com.cpto.dapp.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 管理日志
 *
 * @author sunli
 * @date 2018/12/07
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "log_manager", comment = "管理日志")
public class LogManager extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

}
