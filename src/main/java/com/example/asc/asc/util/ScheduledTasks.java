package com.example.asc.asc.util;

import com.example.asc.asc.trd.asc.applicationfordeposit.ApplicationDepositService;
import com.example.asc.asc.trd.asc.applydepositaccount.domain.ApplyDepositAccount;
import com.example.asc.asc.trd.asc.applydepositaccount.service.ApplyDepositAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 *
 * @author zhanglize
 * @create 2019/10/18
 */
@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private ApplyDepositAccountService accountService;

    private ApplicationDepositService applicationDepositService;

    @Autowired
    public void setApplicationDepositService(ApplicationDepositService applicationDepositService) {
        this.applicationDepositService = applicationDepositService;
    }

    @Autowired
    public void setAccountService(ApplyDepositAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 每天早上9:30 到下午16:40,每隔俩个小时查询一次申请表中的数据查看是否到账
     */
//    @Scheduled(cron = "0 30 9,11,14,16 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
    public void pollT0Task() {
        logger.info("T0 轮询池定时任务 :" + DateUtils.stringToDate());
        String balflag = "T0";
        String status = "交易中";
        //查询申请表中T0的所有状态为处理中的数据信息
        List<ApplyDepositAccount> accountList = accountService.queryFlagStaus(balflag, status, null);
        //查询这些数据的出金结果查询
        if (accountList.size() != 0) {
            accountList.forEach(applyDepositAccount -> applicationDepositService.queryApplicationDeposit(applyDepositAccount.getMsghdTrdt(), applyDepositAccount.getSrlPtnsrl()));
        }
    }

    /**
     * 每天14：00到下午17：00每隔一个小时查询昨天T1的资金到没到帐
     */
//    @Scheduled(cron = "0 0 14,15,16,17 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
    public void pollT1Task() {
        logger.info("T1 轮询池定时任务 :" + DateUtils.stringToDate());
        String balflag = "T1";
        String status = "交易中";
        //查询申请表中昨天T1的所有状态为处理中的数据信息
        List<ApplyDepositAccount> accountList = accountService.queryFlagStaus(balflag, status, DateUtils.yesterDayTime());
        //查询这些数据的出金结果查询
        if (accountList.size() != 0) {
            accountList.forEach(applyDepositAccount -> applicationDepositService.queryApplicationDeposit(applyDepositAccount.getMsghdTrdt(), applyDepositAccount.getSrlPtnsrl()));
        }
    }

}
