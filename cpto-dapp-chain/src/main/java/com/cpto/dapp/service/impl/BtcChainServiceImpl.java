package com.cpto.dapp.service.impl;

import com.cpto.dapp.pojo.dto.BtcChainRecordDTO;
import com.cpto.dapp.service.BtcChainService;
import com.cpto.dapp.util.HttpUtil;
import com.cpto.dapp.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BtcChainServiceImpl implements BtcChainService {

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

        ResponseEntity<Map> response = btc("getnewaddress", id);

        if (response != null) {
            Map responseBody = response.getBody();
            if (responseBody.containsKey("result")) {
                return String.valueOf(responseBody.get("result"));
            }
        }

        return null;
    }

    @Override
    public List<BtcChainRecordDTO> findRecord(String param, int count) {

        List<BtcChainRecordDTO> list = new ArrayList<>();

        String method = "listtransactions";
        ResponseEntity<Map> response = btc(method, new Object[]{param, count});

        if (response != null) {
            Map responseBody = response.getBody();
            if (responseBody.containsKey("result")) {
                String result = JsonUtil.objectToJSon(responseBody.get("result"));
                result = result.replaceAll("bip125-replaceable", "bip125_replaceable");
                list = JsonUtil.jsonToObjectList(result, new TypeReference<List<BtcChainRecordDTO>>() {
                });
            }
        }

        return list;
    }

    private ResponseEntity<Map> btc(String method, Object... param) {

        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("jsonrpc", "2.0");
        map.put("method", method);
        map.put("id", System.currentTimeMillis() + "");

        if (param != null) {
            map.put("params", param);
        }

        String s = btcUser + ":" + btcPassword;
        String creb = Base64.encodeBase64String(s.getBytes());
        ResponseEntity<Map> response = HttpUtil.send(btcUrl, map, "Basic " + creb, HttpMethod.POST, MediaType.APPLICATION_JSON);

        return response;
    }
}