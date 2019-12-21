package com.cpto.dapp.domain;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 项目收益记录
 *
 * @author sunli
 * @date 2019/02/12
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "log_project_income", comment = "项目收益记录")
public class LogProjectIncome extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned comment '项目id'", nullable = false)
    private Long projectId;

    @Column(columnDefinition = "int unsigned comment '发放时项目状态'", nullable = false)
    private Integer projectStatus;

    @Column(columnDefinition = "decimal(18, 6) comment '数额'", nullable = false)
    private BigDecimal amount;
}