package com.example.asc.asc.trd.asc.withdraw;

import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT1018Request;
import com.trz.netwk.api.trd.TrdT1018Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * 查询可 T0/T1 出金额度[T1018]业务层
 *
 * @author zhanglize
 * @create 2019/11/4
 */
@Service
public class WithDrawService {
    private static final Logger logger = LoggerFactory.getLogger(WithDrawService.class);

    /**
     * 查询可 T0/T1 出金额度
     *
     * @return
     */
    public Map<String, String> withDraw(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> treeMap = new TreeMap<>();
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            //交易日期
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghd_trdt"));
            //合作方编号
            String PtnCd = "HLYI2019";
            //托管方编号
            String BkCd = "ZXYH0001";
            /** 资金账号 */
            String cltacc_subno = req.getParameter("cltacc_subno");
            /** 户名 */
            String cltacc_cltnm = req.getParameter("cltacc_cltnm");
            //加载配置文件信息
            FileConfigure.getFileConfigure(cltacc_subno);
            // 2. 实例化交易对象
            TrdT1018Request trdRequest = new TrdT1018Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setCltacc_subno(cltacc_subno);
            trdRequest.setCltacc_cltnm(cltacc_cltnm);
            trdRequest.setMsghd_ptncd(PtnCd);
            trdRequest.setMsghd_bkcd(BkCd);
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
            TrdT1018Response trdResponse = new TrdT1018Response(respMsg);
            logger.info("响应报文[" + trdResponse.getResponsePlainText() + "]");
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                treeMap = getTreeMap(trdResponse, cltacc_subno, cltacc_cltnm);
            } else {
                treeMap = getTreeMap(trdResponse, cltacc_subno, cltacc_cltnm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeMap;
    }

    /**
     * 获取返回消息报文信息
     *
     * @param trdResponse
     * @param cltacc_subno
     * @param cltacc_cltnm
     * @return
     */
    private Map<String, String> getTreeMap(TrdT1018Response trdResponse, String cltacc_subno, String cltacc_cltnm) {
        Map<String, String> treeMap = new TreeMap<>();
        //获取请求报文信息
        treeMap.put("msghd_rspcode", trdResponse.getMsghd_rspcode());
        treeMap.put("msghd_rspmsg", trdResponse.getMsghd_rspmsg());  // 返回信息
        treeMap.put("cltacc_subno", cltacc_subno);  // 资金账号
        treeMap.put("cltacc_cltnm", cltacc_cltnm); // 户名
        treeMap.put("amt_ccycd", trdResponse.getAmt_ccycd()); // 币种，默认“CNY”
        // 账户余额-智融资金账户
        treeMap.put("acsamt_balamt", String.valueOf(trdResponse.getAcsamt_balamt()));  // 资金余额(单位:分)
        treeMap.put("acsamt_useamt", String.valueOf(trdResponse.getAcsamt_useamt()));  // 可用资金
        treeMap.put("acsamt_frzamt", String.valueOf(trdResponse.getAcsamt_frzamt())); // 冻结资金
        // 可T1代付出金的额度
        treeMap.put("t1amt_ctamta00", String.valueOf(trdResponse.getT1amt_ctamta00()));  // 正常出金（A00）时的额度(单位:分)
        treeMap.put("t1amt_ctamtb01", String.valueOf(trdResponse.getT1amt_ctamtb01()));  // 解冻出金（B01）时的额度(单位:分)
        // 可T0代付出金的额度
        treeMap.put("t0amt_ctamta00", String.valueOf(trdResponse.getT0amt_ctamta00())); // 正常出金（A00）时的额度(单位:分)
        treeMap.put("t0amt_ctamtb01", String.valueOf(trdResponse.getT0amt_ctamtb01()));  // 解冻出金（B01）时的额度(单位:分)
        return treeMap;
    }
}
