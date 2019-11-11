package com.example.asc.asc.trd.asc.applicationfordeposit;


import com.example.asc.asc.trd.asc.applydepositaccount.domain.ApplyDepositAccount;
import com.example.asc.asc.trd.asc.applydepositaccount.service.ApplyDepositAccountService;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.asc.useraccountsettlement.domain.UserAccountSettlement;
import com.example.asc.asc.trd.asc.useraccountsettlement.service.UserAccountSettlementService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.GenerateOrderNoUtil;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdCommonResponse;
import com.trz.netwk.api.trd.TrdT2012Request;
import com.trz.netwk.api.trd.TrdT2012Response;
import com.trz.netwk.api.trd.TrdT2022Request;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
    private ApplyDepositAccountService applyDepositAccountService;
    private UserAccountSettlementService settlementService;
    @Autowired
    public void setSettlementService(UserAccountSettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @Autowired
    public void setApplyDepositAccountService(ApplyDepositAccountService applyDepositAccountService) {
        this.applyDepositAccountService = applyDepositAccountService;
    }



    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 出金-申请[T2022]
     *
     * @return
     */
    public BaseResponse applicationDeposit(HttpServletRequest req, HttpServletResponse resp) {
        BaseResponse response = new BaseResponse();
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghd_trdt"));
            /** 合作方交易流水号 */
            String srl_ptnsrl = GenerateOrderNoUtil.gens("eea", 530L);
            /** 资金账号 */
            String cltacc_subno = req.getParameter("subNo");
            UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
            if (userAccount == null) {
                response.setCode("CJ301");
                response.setMsg("资金账号不存在,请核对资金账户信息");
                response.setData(null);
                return response;
            }
            /** 户名 */
            String cltacc_cltnm = userAccount.getName();
            UserAccountSettlement settlement = settlementService.findAccountId(userAccount.getId());
            /** 银行账号(卡号) */
            String bkacc_accno = settlement.getAccNo();
            /** 开户名称 */
            String bkacc_accnm = settlement.getAccNm();
            /** 总金额 */
            long amt_tamt = Long.valueOf(req.getParameter("amt_tamt"));
            //查询单笔金额是否超过5万的额度
            if (amt_tamt > 5000000) {
                response.setCode("CJ302");
                response.setMsg("单笔资金提现总额超过规定额度");
                response.setData(null);
                return response;
            } else if (amt_tamt == 0) {
                response.setCode("CJ303");
                response.setMsg("单笔资金提现总额不能为0或者空");
                response.setData(null);
                return response;
            }
            //根据总金额计算出手续费以及发生额度
            Map<String, Long> map = countAmount(cltacc_subno, amt_tamt);
            /** 发生额(资金单位:分) */
            long amt_aclamt = map.get("amt_aclamt");
            /** 转账手续费 */
            long amt_feeamt = map.get("amt_feeamt");
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
            String balflag = "T0";
            /** 资金用途(附言) */
            String usage = req.getParameter("usage");
            //查询该资金账户在当天是否超过提现次数五次
            if (applyDepositAccountService.queryCount(cltacc_subno, msghd_trdt).size() == 5) {
                response.setCode("CJ304");
                response.setMsg("该资金账户当天提现次数已达上限");
                response.setData(null);
                return response;
            }
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
                insertAccount(trdRequest, trdResponse, "3");
                response = getTreeMap(trdResponse,cltacc_subno);
            } else {
                //交易失败添加到出库申请表中
                insertAccount(trdRequest, trdResponse, "2");
                response = getTreeMap(trdResponse, cltacc_subno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 添加数据到出金申请表中
     *
     * @param trdRequest
     * @param trdResponse
     * @param status
     */
    private void insertAccount(TrdT2022Request trdRequest, TrdCommonResponse trdResponse, String status) {
        ApplyDepositAccount account = new ApplyDepositAccount();
        account.setAmtAclamt(String.valueOf(trdRequest.getAmt_aclamt()));
        account.setAmtFeeamt(String.valueOf(trdRequest.getAmt_feeamt()));
        account.setAmtTamt(String.valueOf(trdRequest.getAmt_tamt()));
        account.setBalflag(trdRequest.getBalflag());
        account.setBkaccAccnm(trdRequest.getBkacc_accnm());
        account.setBkaccAccno(trdRequest.getBkacc_accno());
        account.setCltaccCltnm(trdRequest.getCltacc_cltnm());
        account.setCltaccSubno(trdRequest.getCltacc_subno());
        account.setFeeRate((int) trdRequest.getAmt_feeamt());
        account.setMsghdTrdt(trdRequest.getMsghd_trdt());
        account.setSrlPlatsrl(trdResponse.getSrl_platsrl());
        account.setSrlPtnsrl(trdRequest.getSrl_ptnsrl());
        account.setStatus(status);
        account.setUsage(trdRequest.getUsage());
        applyDepositAccountService.insert(account);
    }

    /**
     * 根据总金额计算出手续费以及发生额度
     *
     * @param cltacc_subno 资金账号
     * @param amt_tamt     总金额
     * @return
     */
    private Map<String, Long> countAmount(String cltacc_subno, long amt_tamt) {
        Map<String, Long> map = new HashMap<>();
        //根据资金账户查询对应的用户申请信息获得用户的费率信息
        UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
        //获取到费率信息
        Integer feeRate = Integer.valueOf(userAccount.getFeeRate());
        /** 转账手续费 */
        long amt_feeamt = amt_tamt * feeRate / 10000;
        //判断手续费是否为0
        if (feeRate == 0) {
            amt_feeamt = 0;
        } else {
            amt_feeamt = amt_tamt * feeRate / 10000;
            if (amt_feeamt == 0) {
                amt_feeamt = 1;
            }
        }
        /** 发生额(资金单位:分) */
        long amt_aclamt = amt_tamt - amt_feeamt;
        map.put("amt_feeamt", amt_feeamt);
        map.put("amt_aclamt", amt_aclamt);
        return map;
    }

    /**
     * 获取出金申请返回消息报文信息
     *
     * @param trdResponse
     * @param cltacc_subno
     * @return
     */
    private BaseResponse getTreeMap(TrdCommonResponse trdResponse, String cltacc_subno) {
        BaseResponse response = new BaseResponse();
        Map<String,String> map = new TreeMap<>();
        //获取请求报文信息
        response.setCode(trdResponse.getMsghd_rspcode());
        response.setMsg(trdResponse.getMsghd_rspmsg());  // 返回信息
        map.put("srl_ptnsrl",trdResponse.getSrl_ptnsrl());  // 合作方流水号
        map.put("subNo",cltacc_subno); //资金账户
        response.setData(JSONObject.fromObject(map));
        return response;
    }

    /**
     * 获取出金结果查询接口信息
     *
     * @param trdResponse
     * @return
     */
    private BaseResponse getQueryTreeMap(TrdT2012Response trdResponse) {
        BaseResponse response = new BaseResponse();
        response.setCode("000000");
        response.setMsg(trdResponse.getMsghd_rspmsg());  // 返回信息
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("srl_ptnsrl", trdResponse.getSrl_ptnsrl()); // 合作方流水号
        treeMap.put("cltacc_subno", trdResponse.getCltacc_subno()); // 子账号
        treeMap.put("cltacc_cltnm", trdResponse.getCltacc_cltnm()); // 户名
        treeMap.put("amt_aclamt", String.valueOf(trdResponse.getAmt_aclamt())); // 发生额
        treeMap.put("amt_aclamt", String.valueOf(trdResponse.getAmt_feeamt())); // 转账手续费
        treeMap.put("amt_ccycd", trdResponse.getAmt_ccycd()); // 币种，默认“CNY”
        treeMap.put("state", trdResponse.getState()); // 交易结果:1成功;2失败;3处理中
        treeMap.put("resttime", trdResponse.getResttime()); // 交易成功/失败时间(渠道通知时间)-出金时指交易成功时间，不是到账时间-格式:YYYYMMDDHH24MISS
        treeMap.put("opion", trdResponse.getMsghd_rspmsg());// 失败原因
        treeMap.put("ubalsta", trdResponse.getUbalsta());// 出金结算状态(查询出金结果时返回)0未结算;1已发送结算申请
        treeMap.put("ubaltim", trdResponse.getUbaltim());// 出金结算时间(查询出金结果时返回)-格式YYYYMMDDHH24MISS-UBalSta=1时指成功发送结算申请的时间
        treeMap.put("usage", trdResponse.getUsage());// 资金用途(附言)
        // 业务标示
        // 入金业务时指：
        // A00 正常入金
        // B00 入金成功后，再冻结资金
        // 出金业务时指：
        // A00 正常出金
        // B01 解冻资金后，再出金
        treeMap.put("trsflag", trdResponse.getTrsflag());// 失败原因
        treeMap.put("fdate", trdResponse.getMsghd_rspmsg());// 原交易日期
        treeMap.put("ftime", trdResponse.getFtime()); // 原交易时间
        return response;
    }

    /**
     * 出入金结果查询[T2012]
     *
     * @return
     */
    public BaseResponse queryApplicationDeposit(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /** 交易日期 */
        String msghd_trdt = req.getParameter("msghd_trdt");
        /** 待查询原交易流水号 */
        String orgsrl = req.getParameter("orgsrl");
        return queryApplicationDeposit(msghd_trdt, orgsrl);
    }

    /**
     * 出入金结果查询[T2012]
     *
     * @param msghd_trdt 交易日期
     * @param orgsrl     待查询原交易流水号
     * @return
     */
    public BaseResponse queryApplicationDeposit(String msghd_trdt, String orgsrl) {
        BaseResponse response = new BaseResponse();
        try {
            ApplyDepositAccount account = applyDepositAccountService.querySrlPtnsrl(orgsrl);
            //加载配置文件信息
            FileConfigure.getFileConfigure(account.getCltaccSubno());
            // 2. 实例化交易对象
            TrdT2012Request trdRequest = new TrdT2012Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setOrgsrl(orgsrl);
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
            TrdT2012Response trdResponse = new TrdT2012Response(respMsg);
            logger.info("响应报文[" + trdResponse.getResponsePlainText() + "]");
            String state = trdResponse.getState();  // 交易结果:1成功;2失败;3处理中
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                account.setStatus(state);
                applyDepositAccountService.update(account.getId(), account);
                //获取到出金结果查询的返回信息
                response = getQueryTreeMap(trdResponse);
            } else {
                account.setStatus(state);
                applyDepositAccountService.update(account.getId(), account);
                response = getQueryTreeMap(trdResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
