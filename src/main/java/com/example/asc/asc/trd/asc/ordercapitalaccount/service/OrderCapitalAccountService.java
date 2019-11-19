package com.example.asc.asc.trd.asc.ordercapitalaccount.service;

import com.blue.util.DateUtil;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.ordercapitalaccount.domain.OrderCapitalAccount;
import com.example.asc.asc.trd.asc.ordercapitalaccount.mapper.OrderCapitalAccountMapper;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.DateUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单支付业务层
 *
 * @author zhanglize
 * @create 2019/11/19
 */
@Service
public class OrderCapitalAccountService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCapitalAccountService.class);
    private UserAccountService userAccountService;
    private OrderCapitalAccountMapper mapper;

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Autowired
    public void setMapper(OrderCapitalAccountMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 订单支付
     *
     * @return
     */
    public BaseResponse orderCapitalAccount(HttpServletRequest req, HttpServletResponse resp) {
        BaseResponse response = new BaseResponse();
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
            String billinfo_rcltnm = getReciveSubbName(req);
            req.getParameter("reciveSubbName");
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
            //加载配置文件信息
            FileConfigure.getFileConfigure(billinfo_psubno);
            FileConfigure.getFileConfigure(billinfo_rsubno);
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
            //判断响应报文的处理信息
            response = judgeResponse(trdRequest, trdResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private BaseResponse judgeResponse(TrdT3004Request trdRequest, TrdT3004Response trdResponse) {
        BaseResponse baseResponse = new BaseResponse();
        if("PYSUCC".equals(trdResponse.getMsghd_rspcode())){
            return reternData(trdRequest,trdResponse);
        }
        // 交易成功 000000
        if ("000000".equals(trdResponse.getMsghd_rspcode())) {
            logger.info("[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]");// 返回信息
            logger.info("[srl_billno]=[" + trdResponse.getSrl_billno() + "]");// 支付单号(唯一)
            logger.info("[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]");// 平台流水号
            //添加数据到数据订单支付表中
            addOrderCapitalAccount(trdRequest, trdResponse);
            baseResponse = reternData(trdRequest,trdResponse);
        } else {
            //添加数据到数据订单支付表中
            addOrderCapitalAccount(trdRequest, trdResponse);
            baseResponse = reternData(trdRequest,trdResponse);
        }
        return baseResponse;
    }


    private BaseResponse reternData(TrdT3004Request trdRequest, TrdT3004Response trdResponse) {
        BaseResponse baseResponse = new BaseResponse();
        Map<String,String> map = new HashMap<>();
        baseResponse.setCode(trdResponse.getMsghd_rspcode());
        baseResponse.setMsg(trdResponse.getMsghd_rspmsg());
        map.put("paySubbNo",trdRequest.getBillinfo_psubno());
        map.put("paySubbName",trdRequest.getBillinfo_pnm());
        map.put("reciveSubbNo",trdRequest.getBillinfo_rsubno());
        map.put("reciveSubbName",trdRequest.getBillinfo_rcltnm());
        baseResponse.setData(map);
        return baseResponse;
    }

    /**
     * 添加数据到数据订单支付表中
     *
     * @param trdRequest
     * @param trdResponse
     */
    private void addOrderCapitalAccount(TrdT3004Request trdRequest, TrdT3004Response trdResponse) {
        OrderCapitalAccount account = new OrderCapitalAccount();
        account.setDate(new Date());
        account.setMoney(trdRequest.getBillinfo_aclamt());
        account.setOrderNo(trdRequest.getBillinfo_orderno());
        account.setPayFee(String.valueOf(trdRequest.getBillinfo_payfee()));
        account.setReciveFee(String.valueOf(trdRequest.getBillinfo_payeefee()));
        account.setPaySubbNo(trdRequest.getBillinfo_psubno());
        account.setReciveSubbNo(trdRequest.getBillinfo_rsubno());
        account.setPlatSrl(trdResponse.getSrl_platsrl());
        if (trdResponse.getMsghd_rspcode().equals("000000")) {
            account.setStatus("1");
        } else {
            account.setStatus("2");
        }
        account.setPtnSrl(trdRequest.getBillinfo_billno());
        account.setUsage(trdRequest.getBillinfo_usage());
        account.setPaySubbName(trdRequest.getBillinfo_pnm());
        account.setReciveSubbName(trdRequest.getBillinfo_rcltnm());
        insert(account);
    }


    /**
     * 获取收款方的账户名称信息
     *
     * @param request
     * @return
     */
    private String getReciveSubbName(HttpServletRequest request) {
        String paySubbName = request.getParameter("reciveSubbName");
        if (StringUtils.isEmpty(paySubbName)) {
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
        if (StringUtils.isEmpty(paySubbName)) {
            //根据付款账户的编号获取到账户名称
            return userAccountService.findBySubNo(request.getParameter("paySubbNo")).getName();
        }
        return paySubbName;
    }

    /**
     * 根据订单流水号查询对应的交易订单信息
     *
     * @param ptnSrl
     * @return
     */
    public OrderCapitalAccount findByPtnSrl(String ptnSrl) {
        return mapper.findByPtnSrl(ptnSrl);
    }

    /**
     * 添加数据到数据库中
     *
     * @param account
     * @return
     */
    private int insert(OrderCapitalAccount account) {
        account.setTime(DateUtil.formatToHms(DateUtils.stringToDate()));
        account.setCreatedAt(DateUtils.toTimestamp()); //创建时间
        return mapper.insert(account);
    }
}
