package com.cpto.dapp.service;


import com.cpto.dapp.pojo.dto.EthChainRecordDTO;
import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;
import java.util.List;

public interface EthChainService {

    String createAccount(String id);

    List<EthChainRecordDTO> findRecord(String address);

    BigInteger getBalance(String address);

    Transaction makeTransaction(String fromAddress, String toAddress,
                                BigInteger nonce, BigInteger gasPrice,
                                BigInteger gasLimit, BigInteger value);

    BigInteger getTransactionGasLimit(Transaction transaction);

    BigInteger getTransactionNonce(String address);
}