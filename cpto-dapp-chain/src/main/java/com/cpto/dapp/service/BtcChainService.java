package com.cpto.dapp.service;


import com.cpto.dapp.pojo.dto.BtcChainRecordDTO;

import java.util.List;

public interface BtcChainService {

    String createAccount(String id);

    List<BtcChainRecordDTO> findRecord(String param, int count);

}