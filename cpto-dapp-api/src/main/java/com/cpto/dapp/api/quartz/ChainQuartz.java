package com.cpto.dapp.api.quartz;

import com.cpto.dapp.service.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 区块链Quartz
 *
 * @author sunli
 * @date 2019/04/11
 */
@Component
public class ChainQuartz {

    @Autowired
    private ChainService chainService;

    /**
     * 同步区块链的转账记录
     * 间隔1分钟触发
     */
// @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0/10 * * * * ?")
    public void syncChainRecord() {
        chainService.syncChainRecord();
    }
}
