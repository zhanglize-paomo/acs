package com.example.asc.asc.util;

import com.example.asc.asc.trd.asc.applicationfordeposit.ApplicationDepositService;
import com.example.asc.asc.trd.asc.applydepositaccount.domain.ApplyDepositAccount;
import com.example.asc.asc.trd.asc.applydepositaccount.service.ApplyDepositAccountService;
import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    private EntryExitAccountService entryExitAccountService;

    @Autowired
    public void setEntryExitAccountService(EntryExitAccountService entryExitAccountService) {
        this.entryExitAccountService = entryExitAccountService;
    }

    @Autowired
    public void setApplicationDepositService(ApplicationDepositService applicationDepositService) {
        this.applicationDepositService = applicationDepositService;
    }

    @Autowired
    public void setAccountService(ApplyDepositAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 每天早上9:30 到下午16:30,每隔俩个小时查询一次申请表中的数据查看是否到账
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

    /**
     * 每隔10分钟查询支付订单信息
     */
    @Scheduled(cron = " 0 0/11 * * * ?")
//    @Scheduled(cron = "1 * * * * ?")
    public void pollOrderTask() {
        logger.info("支付订单 轮询池定时任务 :" + DateUtils.stringToDate());
        //查询所有订单消息的交易中的状态
        List<EntryExitAccount> accountList = entryExitAccountService.findByStatus("0");
        accountList.forEach(account -> {
            long minutes = 0L;
            try {
                //判断该笔订单的交易时间
                String createTime = DateUtils.dateToOnlyTime(account.getCreatedAt().toString());
                //获取当前时间的HH：mm:ss
                String time = DateUtils.nowTime();
                DateFormat df = new SimpleDateFormat("HH:mm");
                Date d1 = df.parse(time);
                Date d2 = df.parse(createTime);
                long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
                minutes = diff / (1000 * 60);
            } catch (ParseException e) {
                logger.info("抱歉，时间日期解析出错");
            }
            if (minutes != 0L) {
                if (minutes > 11 || minutes < 0L) {
                    account.setStatus("2");
                    //请求后台接口,将该笔客户交易流水信息置为交易失败
                    entryExitAccountService.update(account.getId(), account);
                    //并发送消息给下游客户
                    String servnoticeUrl = account.getServnoticeUrl();
                    if (!StringUtils.isEmpty(servnoticeUrl)) {
                        int num = 0;
                        Map<String, Object> hashMap = new TreeMap<>();
                        Map<String, String> map = new TreeMap<>();
                        hashMap.put("code", "000001");
                        hashMap.put("msg", "支付失败");
                        map.put("SrcPtnSrl", account.getPtnSrl());
                        hashMap.put("data", map);
                        //发送消息给下游客户
                        entryExitAccountService.doPostOrGet(servnoticeUrl, hashMap, num, account.getSendToClientTimes(), account);
                    }
                }
            }
        });
    }


}
