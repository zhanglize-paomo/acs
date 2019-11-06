package com.example.asc.asc.trd.asc.applicationfordeposit;


import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdCommonResponse;
import com.trz.netwk.api.trd.TrdT2022Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * 出金-申请[T2022]业务层
 *
 * @author zhanglize
 * @create 2019/11/4
 */
@Service
public class ApplicationDepositService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationDepositService.class);

    private UserAccountService userAccountService;
    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 出金-申请[T2022]
     *
     * @return
     */
    public Map<String, String> applicationDeposit(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> treeMap = new TreeMap<>();
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
            //根据资金账户查询对应的用户申请信息获得用户的费率信息
            UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
            //TODO 总金额 = 发生额+ 转账手续费
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
                treeMap = getTreeMap(trdResponse);
            } else {
                treeMap = getTreeMap(trdResponse);
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
     * @return
     */
    private Map<String, String> getTreeMap(TrdCommonResponse trdResponse) {
        Map<String, String> treeMap = new TreeMap<>();
        //获取请求报文信息
        treeMap.put("msghd_rspcode", trdResponse.getMsghd_rspcode());
        treeMap.put("srl_ptnsrl", trdResponse.getSrl_ptnsrl()); // 合作方流水号
        treeMap.put("srl_platsrl", trdResponse.getSrl_platsrl()); // 平台流水号
        treeMap.put("msghd_rspmsg", trdResponse.getMsghd_rspmsg());  // 返回信息
        return treeMap;
    }
}
