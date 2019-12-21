package com.cpto.dapp.api.quartz;

import com.cpto.dapp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 账户Quartz
 *
 * @author sunli
 * @date 2019/04/11
 */
@Component
public class WalletQuartz {

    @Autowired
    private WalletService walletService;

    /**
     * 根据区块链转账记录进行充值
     * 间隔1分钟触发
     */
// @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0/10 * * * * ?")
    public void topUp() {
        walletService.topUp();
    }
}
