package com.cpto.dapp.repository;

import com.cpto.dapp.domain.SyncEthChainRecord;

import java.util.List;

/**
 * 同步ETH转账记录Repository
 *
 * @author sunli
 * @date 2019/04/11
 */
public interface SyncEthChainRecordRepository extends BaseRepository<SyncEthChainRecord, Long> {

    Boolean existsByHash(String hash);

    List<SyncEthChainRecord> findByIsReadFalse();
}