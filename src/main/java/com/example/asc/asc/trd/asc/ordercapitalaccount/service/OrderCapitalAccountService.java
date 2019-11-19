package com.example.asc.asc.trd.asc.ordercapitalaccount.service;

import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.ordercapitalaccount.mapper.OrderCapitalAccountMapper;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.util.GenerateOrderNoUtil;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdCommonResponse;
import com.trz.netwk.api.trd.TrdT3004Request;
import com.trz.netwk.api.trd.TrdT3004Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单支付业务层
 *
 * @author zhanglize
 * @create 2019/11/19
 */
@Service
public class OrderCapitalAccountService {

    private UserAccountService userAccountService;
    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    private OrderCapitalAccountMapper mapper;
    @Autowired
    public void setMapper(OrderCapitalAccountMapper mapper) {
        this.mapper = mapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(OrderCapitalAccountService.class);

    /**
     * 订单支付
     *
     * @return
     */
    public BaseResponse orderCapitalAccount(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghdTrdt"));
            /** 付款方资金账号 */
            String billinfo_psubno = req.getParameter("paySubbNo");
            /** 付款方账户名称 */
            String billinfo_pnm = getPaySubbName(req);
            /** 收款方资金账号 */
            String billinfo_rsubno = req.getParameter("reciveSubbNo");
            /** 收款方账户名称 */
            String billinfo_rcltnm = getReciveSubbName(req);req.getParameter("reciveSubbName");
            /** 业务单号 */
            String billinfo_orderno = GenerateOrderNoUtil.gens("eea", 530L);
            /** 支付流水号(唯一) */
            String billinfo_billno = req.getParameter("ptnSrl");
            /** 本次支付金额 */
            long billinfo_aclamt = Long.valueOf(req.getParameter("money"));
            /** 付款方手续费,暂定0 */
            long billinfo_payfee = 0;
            /** 收款方手续费,暂定0 */
            long billinfo_payeefee = 0;
            /** 资金用途 */
            String billinfo_usage = "订单支付";
            /** 商品信息 */
            String billinfo_goodsmess = "订单支付";
            /**
             * 业务标示 A00 普通订单支付 B00 收款方支付冻结 [付款冻结] PS：冻结失败，资金回滚 B01 付款方解冻支付 [解冻退款]
             */
            String trsflag = "A00";
            // 2. 实例化交易对象
            TrdT3004Request trdRequest = new TrdT3004Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setBillinfo_psubno(billinfo_psubno);
            trdRequest.setBillinfo_pnm(billinfo_pnm);
            trdRequest.setBillinfo_rsubno(billinfo_rsubno);
            trdRequest.setBillinfo_rcltnm(billinfo_rcltnm);
            trdRequest.setBillinfo_orderno(billinfo_orderno);
            trdRequest.setBillinfo_billno(billinfo_billno);
            trdRequest.setBillinfo_aclamt(billinfo_aclamt);
            trdRequest.setBillinfo_payfee(billinfo_payfee);
            trdRequest.setBillinfo_payeefee(billinfo_payeefee);
            trdRequest.setBillinfo_usage(billinfo_usage);
            trdRequest.setTrsflag(trsflag);
            trdRequest.setBillinfo_goodsmess(billinfo_goodsmess);
            // 3. 报文处理
            trdRequest.process();
            logger.info("请求报文[" + trdRequest.getRequestPlainText() + "]");
            logger.info("签名原文[" + trdRequest.getRequestMessage() + "]");
            logger.info("签名数据[" + trdRequest.getRequestSignature() + "]");
            // 4. 与融资平台通信
            TrdMessenger trdMessenger = new TrdMessenger();
            // message
            String respMsg = trdMessenger.send(trdRequest);
            // 5. 处理交易结果
            TrdT3004Response trdResponse = new TrdT3004Response(respMsg);
            logger.info("响应报文[" + trdResponse.getResponsePlainText() + "]");
            System.out.println();
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                // ！！！ 在这里添加合作方处理逻辑！！！
                logger.info("[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]");// 返回信息
                logger.info("[srl_billno]=[" + trdResponse.getSrl_billno() + "]");// 支付单号(唯一)
                logger.info("[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]");// 平台流水号
            } else {
                // ！！！ 在这里添加合作方处理逻辑！！！
            }
            if (false) {
                final TrdT3004Request trdRequest2 = new TrdT3004Request();
                trdRequest2.setMsghd_trdt(msghd_trdt);
                trdRequest2.setBillinfo_psubno(billinfo_psubno);
                trdRequest2.setBillinfo_pnm(billinfo_pnm);
                trdRequest2.setBillinfo_rsubno(billinfo_rsubno);
                trdRequest2.setBillinfo_rcltnm(billinfo_rcltnm);
                trdRequest2.setBillinfo_orderno(billinfo_orderno);
                trdRequest2.setBillinfo_billno(billinfo_billno);
                trdRequest2.setBillinfo_aclamt(billinfo_aclamt);
                trdRequest2.setBillinfo_payfee(billinfo_payfee);
                trdRequest2.setBillinfo_payeefee(billinfo_payeefee);
                trdRequest2.setBillinfo_usage(billinfo_usage);
                trdRequest2.setTrsflag(trsflag);
                new Thread() {
                    @SuppressWarnings("deprecation")
                    public void run() {
                        try {
                            test_batch(trdRequest2);
                        } catch (Throwable e) {
                        } finally {
                            stop();
                        }

                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取收款方的账户名称信息
     *
     * @param request
     * @return
     */
    private String getReciveSubbName(HttpServletRequest request) {
        String paySubbName = request.getParameter("reciveSubbName");
        if(StringUtils.isEmpty(paySubbName)){
            //根据收款账户的编号获取到账户名称
            return userAccountService.findBySubNo(request.getParameter("reciveSubbNo")).getName();
        }
        return paySubbName;
    }

    /**
     * 获取付款账户的名称
     *
     * @param request
     * @return
     */
    private String getPaySubbName(HttpServletRequest request) {
       String paySubbName = request.getParameter("paySubbName");
        if(StringUtils.isEmpty(paySubbName)){
            //根据付款账户的编号获取到账户名称
            return userAccountService.findBySubNo(request.getParameter("paySubbNo")).getName();
        }
        return paySubbName;
    }

    private void test_batch(TrdT3004Request trdRequest2) {
        if (true) {
            return;
        }
        long start = System.currentTimeMillis();
        TrdT3004Request trdRequest3 = new TrdT3004Request();
        trdRequest3.setMsghd_trdt(trdRequest2.getMsghd_trdt());
        trdRequest3.setBillinfo_psubno(trdRequest2.getBillinfo_psubno());
        trdRequest3.setBillinfo_pnm(trdRequest2.getBillinfo_pnm());
        trdRequest3.setBillinfo_rsubno(trdRequest2.getBillinfo_rsubno());
        trdRequest3.setBillinfo_rcltnm(trdRequest2.getBillinfo_rcltnm());
        trdRequest3.setBillinfo_orderno(trdRequest2.getBillinfo_orderno());
        trdRequest3.setBillinfo_aclamt(trdRequest2.getBillinfo_aclamt());
        trdRequest3.setBillinfo_payfee(trdRequest2.getBillinfo_payfee());
        trdRequest3.setBillinfo_payeefee(trdRequest2.getBillinfo_payeefee());
        trdRequest3.setBillinfo_usage(trdRequest2.getBillinfo_usage());
        trdRequest3.setTrsflag("A00");
        for (int i = 1; i < 10000; i++) {
            try {
                logger.info("==============START i=" + i);
                trdRequest3.setBillinfo_billno(trdRequest2.getBillinfo_billno() + "_" + i);
                trdRequest3.process();
                logger.info("请求报文[" + trdRequest3.getRequestPlainText() + "]");
                // 4. 与融资平台通信
                TrdMessenger trdMessenger = new TrdMessenger();
                // message
                String respMsg = trdMessenger.send(trdRequest3);

                // 5. 处理交易结果
                TrdCommonResponse trdResponse = new TrdCommonResponse(respMsg);
                logger.info("响应报文[" + trdResponse.getResponsePlainText() + "]");
                logger.info("==============END i=" + i);
            } catch (Throwable e) {
            }
        }
        long end = System.currentTimeMillis();
        logger.info("测试结束，总耗时=" + (end - start) + "毫秒");
    }
}
