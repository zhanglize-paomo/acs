package com.example.asc.asc.trd.asc.freezingthawingno;

import com.example.asc.asc.trd.common.BaseResponse;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdCommonResponse;
import com.trz.netwk.api.trd.TrdT3001Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 冻结/解冻业务逻辑信息
 *
 * @author zhanglize
 * @create 2019/12/30
 */
@Service
public class FreezingThawingNoService {

    private static final String TAG = "{冻结/解冻}-";
    private static final Logger logger = LoggerFactory.getLogger(FreezingThawingNoService.class);

    /**
     * 解冻[T3001]
     *
     * @param req
     * @param resp
     * @return
     */
    public BaseResponse freezingThawingNo(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = req.getParameter("msghd_trdt");
            /** 合作方交易流水号 */
            String srl_ptnsrl = req.getParameter("srl_ptnsrl");
            /** 资金账号 */
            String cltacc_subno = req.getParameter("cltacc_subno");
            /** 户名 */
            String cltacc_cltnm = req.getParameter("cltacc_cltnm");
            /** 发生额(资金单位:分) */
            long amt_aclamt = Long.valueOf(req.getParameter("amt_aclamt"));
            /** 资金用途(附言) */
            String usage = req.getParameter("usage");
            /** 冻结业务标示(A00冻结;B00解冻) */
            String trsflag = "B00";
            // 2. 实例化交易对象
            TrdT3001Request trdRequest = new TrdT3001Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setSrl_ptnsrl(srl_ptnsrl);
            trdRequest.setCltacc_subno(cltacc_subno);
            trdRequest.setCltacc_cltnm(cltacc_cltnm);
            trdRequest.setAmt_aclamt(amt_aclamt);
            trdRequest.setUsage(usage);
            trdRequest.setTrsflag(trsflag);
            // 3. 报文处理
            trdRequest.process();
            logger.info(TAG + "请求报文[" + trdRequest.getRequestPlainText() + "]");
            logger.info(TAG + "签名原文[" + trdRequest.getRequestMessage() + "]");
            logger.info(TAG + "签名数据[" + trdRequest.getRequestSignature() + "]");
            // 4. 与融资平台通信
            TrdMessenger trdMessenger = new TrdMessenger();
            // message
            String respMsg = trdMessenger.send(trdRequest);
            // 5. 处理交易结果
            TrdCommonResponse trdResponse = new TrdCommonResponse(respMsg);
            logger.info(TAG + "响应报文[" + trdResponse.getResponsePlainText() + "]");
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                logger.info(TAG + "[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]");  // 返回信息
                logger.info(TAG + "[srl_ptnsrl]=[" + trdResponse.getSrl_ptnsrl() + "]"); // 合作方流水号
                logger.info(TAG + "[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]"); // 平台流水号
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
