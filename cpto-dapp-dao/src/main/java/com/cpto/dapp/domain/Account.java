package com.cpto.dapp.domain;


import com.cpto.dapp.common.constant.Constant;
import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.IdUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 账户
 *
 * @author sunli
 * @date 2019/01/15
 */
@Data
@Entity
@org.hibernate.annotations.Table(appliesTo = "account", comment = "账户")
public class Account extends Base {

    @Value("${spring.profiles.active}")
    @Ignore
    private String profilesActive;

    @Id
    @Column(columnDefinition = "bigint unsigned comment 'id'", nullable = false)
    private Long id;

    @Column(columnDefinition = "bigint unsigned comment '用户id'", nullable = false)
    private Long userId;

    @Column(columnDefinition = "int unsigned comment '账户类型（1：可用账户，2：锁定账户，3：待审核账户）'", nullable = false)
    private Integer accountType;

    @Column(columnDefinition = "decimal(18, 6) unsigned comment '数额'", nullable = false)
    private BigDecimal amount;

    /**
     * 构造方法
     */
    public Account() {

    }

    /**
     * 构造方法
     *
     * @param userId      用户id
     * @param accountType 账户类型
     */
    public Account(Long userId, Integer accountType) {
        this.id = IdUtil.generateIdByCurrentTime();
        this.userId = userId;
        this.accountType = accountType;
        this.amount = BigDecimal.ZERO;
        super.setStatus(1);
        super.setCreatedTime(DateUtil.now());

        // 设置测试数据
        if (ObjectUtil.notEquals(profilesActive, Constant.PROD)) {
            this.amount = new BigDecimal("10000000");
        }
    }
}
