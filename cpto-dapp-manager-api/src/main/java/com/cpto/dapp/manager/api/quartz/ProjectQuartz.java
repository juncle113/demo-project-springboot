package com.cpto.dapp.manager.api.quartz;

import com.cpto.dapp.service.OrderService;
import com.cpto.dapp.service.UserIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 项目定时任务
 *
 * @author sunli
 * @date 2019/03/11
 */
@Component
public class ProjectQuartz {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserIncomeService userIncomeService;

    /**
     * 完成订单（订单锁仓释放时间到期后，释放投资）
     * 间隔10分钟触发
     */
    @Scheduled(cron = "0 2/10 * * * ?")
    public void completeOrder() {
        orderService.completeOrder();
    }

    /**
     * 释放收益：订单完成（及时释放）或者未完成（等待订单完成）
     * 间隔10分钟触发
     */
    @Scheduled(cron = "0 4/10 * * * ?")
    public void returnIncome() {
        userIncomeService.returnIncome();
    }

    // TODO【待定】运营中的收益？
    // TODO【必须】计算app首页的投资和收益总量
}