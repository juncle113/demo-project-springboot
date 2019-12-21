package com.cpto.dapp.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;

public class ChainUtil {

    private static String RPC_URL = "http://47.254.34.46:35147";

    public static Admin admin = Admin.build(new HttpService(RPC_URL));
    public static Web3j web3j = Web3j.build(new HttpService(RPC_URL));
    public static BigDecimal defaultGasPrice = BigDecimal.valueOf(50);

    public static String generateEthPassword(String id) {
        return "da".concat(id).concat("pp");
    }
}
