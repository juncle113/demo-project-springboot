package com.cpto.dapp.service.impl;

import com.cpto.dapp.pojo.dto.EthChainRecordDTO;
import com.cpto.dapp.service.EthChainService;
import com.cpto.dapp.util.ChainUtil;
import com.cpto.dapp.util.HttpUtil;
import com.cpto.dapp.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EthChainServiceImpl implements EthChainService {

    @Value("${custom.chain.btc.user}")
    private String btcUser;
    @Value("${custom.chain.btc.password}")
    private String btcPassword;
    @Value("${custom.chain.btc.url}")
    private String btcUrl;
    @Value("${custom.chain.eth.url}")
    private String ethUrl;

    @Override
    public String createAccount(String id) {

        String password = ChainUtil.generateEthPassword(String.valueOf(id));
        ResponseEntity<Map> response = eth("personal_newAccount", new String[]{password});

        if (response != null) {
            Map responseBody = response.getBody();
            if (responseBody.containsKey("result")) {
                return String.valueOf(responseBody.get("result"));
            }
        }

        return null;
    }

    @Override
    public List<EthChainRecordDTO> findRecord(String address) {

        List<EthChainRecordDTO> list = new ArrayList<>();

        String url = "http://api.etherscan.io/api" + "?module=account&action=txlist&address=" + address + "&startblock=&endblock=&page=1&offset=100&sort=desc&apikey=" + "JXFVM38SRZ88BXYX1BKAAHNVFYYCABYXKE";
        ResponseEntity<Map> response = HttpUtil.send(url, null, null, HttpMethod.POST, MediaType.APPLICATION_JSON);
        if (response != null) {
            Map responseBody = response.getBody();
            if ("1".equals(String.valueOf(responseBody.get("status")))) {
                String result = JsonUtil.objectToJSon(responseBody.get("result"));
                list = JsonUtil.jsonToObjectList(result, new TypeReference<List<EthChainRecordDTO>>() {
                });

            }
        } else {
            log.error("eth 转账记录访问错误");
        }

        return list;
    }

    @Override
    public BigInteger getBalance(String address) {

        ResponseEntity<Map> response = eth("eth_getBalance", new String[]{address, "latest"});
        if (response == null) {
            log.error(address + ":获取eth余额失败");
        } else {
            Map responseBody = response.getBody();
            if (responseBody.containsKey("result")) {
                return new BigInteger(String.valueOf(responseBody.get("result")).replace("0x", ""), 16);
            }
        }
        return null;
    }

    /**
     * 生成一个普通交易对象
     *
     * @param fromAddress 放款方
     * @param toAddress   收款方
     * @param nonce       交易序号
     * @param gasPrice    gas 价格
     * @param gasLimit    gas 数量
     * @param value       金额
     * @return 交易对象
     */
    @Override
    public Transaction makeTransaction(String fromAddress, String toAddress,
                                       BigInteger nonce, BigInteger gasPrice,
                                       BigInteger gasLimit, BigInteger value) {
        Transaction transaction;
        transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
        return transaction;
    }

    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    @Override
    public BigInteger getTransactionGasLimit(Transaction transaction) {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            EthEstimateGas ethEstimateGas = ChainUtil.web3j.ethEstimateGas(transaction).send();
            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gasLimit;
    }

    /**
     * 获取账号交易次数 nonce
     *
     * @param address 钱包地址
     * @return nonce
     */
    @Override
    public BigInteger getTransactionNonce(String address) {
        BigInteger nonce = BigInteger.ZERO;
        try {
            EthGetTransactionCount ethGetTransactionCount = ChainUtil.web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            nonce = ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nonce;
    }

    private ResponseEntity<Map> eth(String method, Object... param) {

        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("jsonrpc", "2.0");
        map.put("method", method);
        map.put("id", "1");

        if (param != null) {
            map.put("params", param);
        }

        ResponseEntity<Map> response = HttpUtil.send(ethUrl, map, null, HttpMethod.POST, MediaType.APPLICATION_JSON);

        return response;
    }
}