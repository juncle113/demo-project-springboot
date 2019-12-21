package com.cpto.dapp.service;

import com.cpto.dapp.domain.Account;
import com.cpto.dapp.pojo.dto.TransferAccountDTO;
import com.cpto.dapp.pojo.vo.AccountAssetsVO;
import com.cpto.dapp.pojo.vo.AccountLogVO;
import com.cpto.dapp.pojo.vo.PageVO;

import java.sql.Timestamp;

/**
 * 账户Service
 *
 * @author sunli
 * @date 2019/01/15
 */
public interface AccountService extends BaseService {

    /**
     * 初始化账户
     *
     * @param userId 用户id
     */
    void initAccount(Long userId);

    /**
     * 转账处理
     *
     * @param transferAccountDTO 转账信息
     */
    void transferAccount(TransferAccountDTO transferAccountDTO);

    /**
     * 根据用户id查询账户记录列表
     *
     * @param searchTime        查询时间
     * @param page              当前页数
     * @param pageSize          每页条数
     * @param userId            用户id
     * @param transferTypeGroup 转账类型分组
     * @param fromCreatedTime   创建开始时间
     * @param toCreatedTime     创建结束时间
     * @return 账户记录列表
     */
    PageVO<AccountLogVO> searchAccountLog(Timestamp searchTime,
                                          Integer page,
                                          Integer pageSize,
                                          Long userId,
                                          Integer transferTypeGroup,
                                          String fromCreatedTime,
                                          String toCreatedTime);

    /**
     * 根据用户id查询账户资产信息
     *
     * @param userId 用户id
     * @return 账户资产信息
     */
    AccountAssetsVO findAssets(Long userId);

    /**
     * 根据用户id和账户类型查询账户信息
     *
     * @param accountType 账户类型
     * @return 账户信息
     */
    Account findAccount(Long userId, Integer accountType);
}