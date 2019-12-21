package com.cpto.dapp.repository;

import com.cpto.dapp.domain.SyncBtcChainRecord;

import java.util.List;

/**
 * 同步BTC转账记录Repository
 *
 * @author sunli
 * @date 2019/04/11
 */
public interface SyncBtcChainRecordRepository extends BaseRepository<SyncBtcChainRecord, Long> {

    Boolean existsByTxId(String txId);

    List<SyncBtcChainRecord> findByIsReadFalse();
}