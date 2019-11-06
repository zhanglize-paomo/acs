package com.example.asc.asc.util;

import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author zhanglize
 * @create 2019/10/18
 */
@Component
public class ScheduledTasks {



//    /**
//     * 轮询池定时任务,每天晚上00:00对于轮询池中的状态,以及单日的交易金额总量进行清0
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
//    public void pollTask() {
//        logger.info("轮询池定时任务 :" + DateUtils.stringToDate());
//        //查询所有状态为完成状态的1所对应的个体商户配置信息
//        List<MerchantConfigure> configureList = configureService.findByStstus("1");
//        configureList.forEach(merchantConfigure -> {
//            merchantConfigure.setTotalOneAmount("0");
//            merchantConfigure.setStatus("0");
//            configureService.update(merchantConfigure.getId(), merchantConfigure.getTotalOneAmount(), merchantConfigure.getStatus());
//        });
//        //查询所有状态为未完成状态0所对应的个体工商户信息
//        List<MerchantConfigure> list = configureService.findByStstus("0");
//        list.forEach(merchantConfigure -> {
//            merchantConfigure.setTotalOneAmount("0");
//            merchantConfigure.setStatus("0");
//            configureService.update(merchantConfigure.getId(), merchantConfigure.getTotalOneAmount(), merchantConfigure.getStatus());
//        });
//    }


}
