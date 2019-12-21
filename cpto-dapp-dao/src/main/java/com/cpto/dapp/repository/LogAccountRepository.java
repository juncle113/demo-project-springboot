package com.cpto.dapp.repository;


import com.cpto.dapp.domain.LogAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户记录Repository
 *
 * @author sunli
 * @date 2019/01/15
 */
public interface LogAccountRepository extends BaseRepository<LogAccount, Long>, JpaSpecificationExecutor<LogAccount> {

    /**
     * 计算该用户的转账合计
     *
     * @param userId           项目id
     * @param transferTypeList 转账类型
     * @param statusList       状态
     * @return 转账合计
     */
    @Query(nativeQuery = true, value = "select ifnull(sum(amount), 0) from log_account where user_id = :userId and transfer_type in (:transferTypeList) and status in (:statusList)")
    BigDecimal sumIncomeAmountByUserId(Long userId, List<Integer> transferTypeList, List<Integer> statusList);
}