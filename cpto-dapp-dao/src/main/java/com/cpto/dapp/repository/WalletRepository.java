package com.cpto.dapp.repository;


import com.cpto.dapp.domain.Wallet;

import java.util.List;

/**
 * 钱包Repository
 *
 * @author sunli
 * @date 2019/01/15
 */
public interface WalletRepository extends BaseRepository<Wallet, Long> {

    /**
     * 根据用户id查询钱包列表
     *
     * @param userId            用户id
     * @param walletAddressType 钱包地址类型
     * @return 钱包列表
     */
    List<Wallet> findByUserIdAndAddressTypeOrderByIdDesc(Long userId, Integer walletAddressType);

    /**
     * 根据钱包地址查询钱包信息
     *
     * @param address 钱包地址
     * @return 钱包信息
     */
    Wallet findByAddress(String address);
}