package com.example.asc.asc.trd.applicationfordeposit;

import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdCommonResponse;
import com.trz.netwk.api.trd.TrdT2022Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 出金-申请[T2022]业务层
 *
 * @author zhanglize
 * @create 2019/11/4
 */
@Service
public class ApplicationDepositService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationDepositService.class);

    /**
     * 出金-申请[T2022]
     *
     * @return
     */
    public Map<String, String> applicationDeposit(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghd_trdt"));
            /** 合作方交易流水号 */
            String srl_ptnsrl = req.getParameter("srl_ptnsrl");
            /** 资金账号 */
            String cltacc_subno = req.getParameter("cltacc_subno");
            /** 户名 */
            String cltacc_cltnm = req.getParameter("cltacc_cltnm");
            /** 银行账号(卡号) */
            String bkacc_accno = req.getParameter("bkacc_accno");
            /** 开户名称 */
            String bkacc_accnm = req.getParameter("bkacc_accnm");
            /** 发生额(资金单位:分) */
            long amt_aclamt = Long.valueOf(req.getParameter("amt_aclamt"));
            /** 转账手续费 */
            long amt_feeamt = Long.valueOf(req.getParameter("amt_feeamt"));
            /** 总金额 */
            long amt_tamt = Long.valueOf(req.getParameter("amt_tamt"));
            //合作方编号
            String PtnCd = "HLYI2019";
            //托管方编号
            String BkCd = "ZXYH0001";
            /**
             * 结算方式标示 <br>
             * AA=正常结算(默认) <br>
             * T0=T0代付出金 <br>
             * T1=T1代付出金
             */
            String balflag = req.getParameter("balflag");
            /** 资金用途(附言) */
            String usage = req.getParameter("usage");
            //加载配置文件信息
            FileConfigure.getFileConfigure(cltacc_subno);
            // 2. 实例化交易对象
            TrdT2022Request trdRequest = new TrdT2022Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setSrl_ptnsrl(srl_ptnsrl);
            trdRequest.setCltacc_subno(cltacc_subno);
            trdRequest.setCltacc_cltnm(cltacc_cltnm);
            trdRequest.setBkacc_accno(bkacc_accno);
            trdRequest.setBkacc_accnm(bkacc_accnm);
            trdRequest.setAmt_aclamt(amt_aclamt);
            trdRequest.setAmt_feeamt(amt_feeamt);
            trdRequest.setBalflag(balflag);
            trdRequest.setUsage(usage);
            trdRequest.setAmt_tamt(amt_tamt);
            trdRequest.setMsghd_ptncd(PtnCd);
            trdRequest.setMsghd_bkcd(BkCd);

//            String orderlist_billnos[] = req.getParameterValues("orderlist_billno");
//            String orderlist_ordernos[] = req.getParameterValues("orderlist_orderno");
//            String orderlist_recvids[] = req.getParameterValues("orderlist_recvid");
//            String orderlist_recvnms[] = req.getParameterValues("orderlist_recvnm");
//            String orderlist_payids[] = req.getParameterValues("orderlist_payid");
//            String orderlist_paynms[] = req.getParameterValues("orderlist_paynm");
//            String orderlist_condates[] = req.getParameterValues("orderlist_condate");
//            String orderlist_conamts[] = req.getParameterValues("orderlist_conamt");
//            String orderlist_payamts[] = req.getParameterValues("orderlist_payamt");
//            String orderlist_paydates[] = req.getParameterValues("orderlist_paydate");
//            String orderlist_ordermemos[] = req.getParameterValues("orderlist_ordermemo");

//            List<OrderList> reqList = new ArrayList<OrderList>();
//            int size = orderlist_billnos.length;
//            for (int i = 0; i < size; i++) {
//                String orderlist_billno = orderlist_billnos[i];
//                if (StringUtil.isNotEmpty(orderlist_billno)) {
//                    OrderList orderList = new OrderList();
//                    String orderlist_orderno = orderlist_ordernos[i];
//                    String orderlist_recvid = orderlist_recvids[i];
//                    String orderlist_recvnm = orderlist_recvnms[i];
//                    String orderlist_payid = orderlist_payids[i];
//                    String orderlist_paynm = orderlist_paynms[i];
//                    String orderlist_condate = orderlist_condates[i];
//                    String orderlist_paydate = orderlist_paydates[i];
//                    String orderlist_ordermemo = orderlist_ordermemos[i];
//                    long orderlist_conamt = MoneyUtil.toNumber(orderlist_conamts[i]).longValue();
//                    long orderlist_payamt = MoneyUtil.toNumber(orderlist_payamts[i]).longValue();
//                    orderList.setBillno(orderlist_billno);
//                    orderList.setOrderno(orderlist_orderno);
//                    orderList.setRecvid(orderlist_recvid);
//                    orderList.setRecvnm(orderlist_recvnm);
//                    orderList.setPayid(orderlist_payid);
//                    orderList.setPaynm(orderlist_paynm);
//                    orderList.setCondate(orderlist_condate);
//                    orderList.setConamt(orderlist_conamt);
//                    orderList.setPayamt(orderlist_payamt);
//                    orderList.setPaydate(orderlist_paydate);
//                    orderList.setOrdermemo(orderlist_ordermemo);
//                    reqList.add(orderList);
//                }
//            }
//            trdRequest.setOrderListList(reqList);
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
            TrdCommonResponse trdResponse = new TrdCommonResponse(respMsg);
            logger.info("响应报文[" + trdResponse.getResponsePlainText() + "]");
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                //如果出金交易成功
                logger.info("[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]");  // 返回信息
                logger.info("[srl_ptnsrl]=[" + trdResponse.getSrl_ptnsrl() + "]");  // 合作方流水号
                logger.info("[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]"); // 平台流水号
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
