package com.cpto.dapp.repository;


import com.cpto.dapp.domain.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 账户Repository
 *
 * @author sunli
 * @date 2019/01/15
 */
public interface AccountRepository extends BaseRepository<Account, Long> {

    /**
     * 根据用户id查询用户的所有账户信息
     *
     * @param userId 用户id
     * @return 账户信息
     */
    List<Account> findByUserId(Long userId);

    /**
     * 根据用户id和账户类型查询账户信息
     *
     * @param userId      用户id
     * @param accountType 账户类型
     * @return 账户信息
     */
    Account findByUserIdAndAccountType(Long userId, Integer accountType);

    /**
     * 改变账户数额
     *
     * @param userId       用户id
     * @param accountType  账户类型
     * @param amount       改变数额（增加为正数，减少为负数）
     * @param modifiedTime 更新时间
     * @return 影响行数
     */
//    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "update account set amount = amount + :amount, modified_time = :modifiedTime where user_id = :userId and account_type = :accountType and amount + :amount >= 0")
    Integer modifyAmount(Long userId, Integer accountType, BigDecimal amount, Timestamp modifiedTime);
}