package com.cpto.dapp.repository;


import com.cpto.dapp.domain.WithdrawalApplication;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 提币申请Repository
 *
 * @author sunli
 * @date 2019/04/14
 */
public interface WithdrawalApplicationRepository extends BaseRepository<WithdrawalApplication, Long>, JpaSpecificationExecutor<WithdrawalApplication> {

}