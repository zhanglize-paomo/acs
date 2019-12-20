package com.example.asc.asc.util;

import com.example.asc.AscApplication;
import com.example.asc.asc.trd.asc.applicationfordeposit.ApplicationDepositService;
import com.example.asc.asc.trd.asc.applydepositaccount.domain.ApplyDepositAccount;
import com.example.asc.asc.trd.asc.applydepositaccount.service.ApplyDepositAccountService;
import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.users.domain.Users;
import com.example.asc.asc.trd.asc.users.service.UsersService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
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
    private static final String TAG_ = "{入金支付异步}-";

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private ApplyDepositAccountService accountService;

    private ApplicationDepositService applicationDepositService;

    private EntryExitAccountService entryExitAccountService;

    private UsersService usersService;

    public static void main(String[] args) {
        String str = "{\"code\":\"000000\",\"msg\":\"交易成功\",\"data\":{\"subNo\":\"1933216000190594\",\"t0amt_ctamta00\":\"17998.93\"}}";
        Map<Object, Object> map = StringUtil.jsonToMap(str);
    }

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

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
     * 每天晚上23:45
     * 查询今天得到账信息,将李艳青的T1账户信息全部划转到对应的浦仕林账户中
     */
    @Scheduled(cron = "0 45 23 * * ?")
    //@Scheduled(cron = "1 * * * * ?")
    public void pollTransferAccounts() {
        logger.info("订单支付的定时任务 :" + DateUtils.stringToDate());
        String pathUrl = "http://39.107.40.13:8080/with-draw?subNo=1933216000190594";
        TreeMap<String, Object> data = new TreeMap<>();
        data.put("subNo", "1933216000190594");
        data.put("flag", "flag");
        String str = HttpUtil2.doGet(pathUrl, data);
        String code = StringUtil.jsonToMap(str).get("code").toString();
        if (code.equals("000000")) {
            String t1 = StringUtil.jsonToMap(StringUtil.jsonToMap(str).get("data")).get("t1amt_ctamta00").toString();
            String orderUrl = "http://39.107.40.13:8080/order-capital-account/orderpay";
            TreeMap<String, Object> map = new TreeMap<>();
            data.put("money", t1);
            data.put("paySubbNo", "1933216000190594");
            data.put("reciveSubbNo", "1934714000194298");
            data.put("ptnSrl", GenerateOrderNoUtil.gens("eea",530L));
            HttpUtil2.doPost(orderUrl, map,"utf-8");
        }
    }

    /**
     * 每隔10分钟查询支付订单信息
     */
    @Scheduled(cron = " 0 0/10 * * * ?")
    //@Scheduled(cron = "1 * * * * ?")
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
                if (minutes > 10 || minutes < 0L) {
                    logger.info(TAG_ + "定时任务支付交易失败：" + account.getId());
                    account.setStatus("2");
                    //请求后台接口,将该笔客户交易流水信息置为交易失败
                    entryExitAccountService.update(account.getId(), account);
                    //并发送消息给下游客户
                    String servnoticeUrl = account.getServnoticeUrl();
                    if (!StringUtils.isEmpty(servnoticeUrl)) {
                        int num = 0;
                        TreeMap<String, Object> hashMap = new TreeMap<>();
                        Map<String, String> map = new TreeMap<>();
                        hashMap.put("code", "000001");
                        hashMap.put("msg", "支付失败");
                        map.put("SrcPtnSrl", account.getPtnSrl());
                        hashMap.put("data", map);
                        //对数据进行签名验证
                        EntryExitAccount entryExitAccount = entryExitAccountService.findByPtnSrl(account.getPtnSrl());
                        Users users = usersService.findById(entryExitAccount.getUserId());
                        String string = entryExitAccountService.checkDigest(users.getAppId(), hashMap);
                        hashMap.put("digest", string);
                        logger.info(TAG_ + "轮询池定时任务返回给下游的地址信息" + account.getServnoticeUrl());
                        logger.info(TAG_ + "轮询池定时任务返回给下游的数据信息" + hashMap);
                        //发送消息给下游客户
                        entryExitAccountService.doPostOrGet(servnoticeUrl, hashMap, num, account.getSendToClientTimes(), account);
                    }
                }
            }
        });
    }


//    @Scheduled(cron = "1 * * * * ?")
//    public void OrderTask() {
//        logger.info("支付订单 轮询池定时任务 :" + DateUtils.stringToDate());
//        String text = "<?xml version=\"1.0\" encoding=\"GBK\"?><MSG version=\"1.5\"><MSGHD><TrCd>T2008</TrCd><TrDt>20191207</TrDt><TrTm>104600</TrTm><TrSrc>R</TrSrc><PtnCd>HLYI2019</PtnCd><BkCd>YLYH0001</BkCd></MSGHD><CltAcc><CltNo>Ol6aBIBCeorcw9eTWq4=b6GD=IshIW</CltNo><SubNo>1933216000190594</SubNo><CltNm>李艳清</CltNm></CltAcc><Amt><AclAmt>99972</AclAmt><FeeAmt>0</FeeAmt><TAmt>99972</TAmt><CcyCd>CNY</CcyCd></Amt><BkAcc></BkAcc><Srl><PlatSrl>1934000013363127</PlatSrl><SrcPtnSrl>Y157564448910335835</SrcPtnSrl></Srl><TrsFlag>1</TrsFlag><Usage>H5支付</Usage><DTrsFlag>A00</DTrsFlag><RestTime>20191206230328</RestTime><State>1</State><FDate>20191206</FDate><FTime>230129</FTime></MSG>\n";
//        //查询所有订单消息的交易中的状态
//        Map<Object, Object> toXmlMap = com.example.asc.asc.util.StringUtil.jsonToMap(XmlUtil.xmlStrToMap(text).get("MSG"));
//        String SrcPtnSrl = com.example.asc.asc.util.StringUtil.jsonToMap(toXmlMap.get("Srl")).get("SrcPtnSrl").toString();
//        TreeMap<String,Object> treeMap = entryExitAccountService.getDownstream(toXmlMap,"支付成功",SrcPtnSrl,"000000");
//        System.out.println(treeMap);
//    }
}
