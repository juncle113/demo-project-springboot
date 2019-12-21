package com.cpto.dapp.pojo.dto;

import lombok.Data;

@Data
public class BtcChainRecordDTO {
    private String account;
    private String address;
    private String category;
    private String amount;
    private String label;
    private String vout;
    private String confirmations;
    private String blockhash;
    private String blockindex;
    private String blocktime;
    private String txid;
    private String[] walletconflicts;
    private String time;
    private String timereceived;
    private String bip125_replaceable;

}
