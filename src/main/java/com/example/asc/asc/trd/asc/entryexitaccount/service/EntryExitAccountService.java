package com.example.asc.asc.trd.asc.entryexitaccount.service;

import com.blue.system.CodeException;
import com.blue.util.StringUtil;
import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.mapper.EntryExitAccountMapper;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.DateUtils;
import com.example.asc.asc.util.GenerateOrderNoUtil;
import com.trz.netwk.api.ntc.NoticeRequest;
import com.trz.netwk.api.ntc.NoticeResponse;
import com.trz.netwk.api.ntc.NtcBaseResponse;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT2031Request;
import com.trz.netwk.api.trd.TrdT2031Response;
import com.trz.netwk.api.v6.ntc.NtcK5129Request;
import com.trz.netwk.api.v6.ntc.NtcK5224Request;
import com.trz.netwk.api.v6.ntc.NtcK5242Request;
import com.trz.netwk.api.v6.ntc.NtcK5246Request;
import com.trz.netwk.api.v6.vo.BusiBill;
import com.trz.netwk.api.v6.vo.Plan;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 入金直通车_异步交易[T2031]业务层
 *
 * @author zhanglize
 * @create 2019/11/8
 */
@Service
public class EntryExitAccountService {

    private static final Logger logger = LoggerFactory.getLogger(EntryExitAccountService.class);

    private UserAccountService userAccountService;
    private EntryExitAccountMapper mapper;

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Autowired
    public void setMapper(EntryExitAccountMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * H5支付,调用云闪付的平台进行支付
     *
     * @return
     */
    public BaseResponse scantoPay(HttpServletRequest req, HttpServletResponse resp) {
        BaseResponse response = new BaseResponse();
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghdTrdt"));
            /** 合作方交易流水号 */
            String srl_ptnsrl = req.getParameter("ptnsrl");
            /** 资金账号 */
            String cltacc_subno = req.getParameter("subNo");
            UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
            /** 户名 */
            String cltacc_cltnm = userAccount.getName();
            /** 支付金额 */
            String billinfo_aclamt = req.getParameter("money");
            /** 支付方式：2：网银;5：快捷支付;6：正扫支付;8：公众号支付;9：银联无卡支付;A：手机APP跳转支付 */
            String billinfo_paytype = "H";
            /** 支付方式-二级分类(1：企业网银PayType=2必输;2：个人网银PayType=2必输;3：支付宝PayType=6/8/A必输;4：微信PayType=6/8/A必输；5：银联PayType=6必输) */
            String billinfo_secpaytype = "5";
            /** 订单标题:PayType=6/8/A时必输 */
            String billinfo_subject = req.getParameter("subject");
            /** 商品描述:PayType=6/8/A时必输 */
            String billinfo_goodsdesc = req.getParameter("goodsdesc");
            /** 是否小程序支付 0 不是 1 是 */
            String billinfo_minitag = "0";
            /** 发送端标记:0手机;1PC端 */
            String reqflg = "1";
            /** 页面通知URL */
            String notificationurl = req.getParameter("notificationurl");
            /** 后台通知URL-若不传值则默认按照后台配置的地址进行通知交易结果 */
            String servnoticurl = "localhost:8080/entry-exit-account/orderscantopay";
            /** 资金用途(附言) */
            String usage = "H5支付";
            /** 合作方自定义备注1 */
            String dremark1 = req.getParameter("dremark1");
            /** 合作方自定义备注2 */
            String dremark2 = req.getParameter("dremark2");
            /** 合作方自定义备注3 */
            String dremark3 = req.getParameter("dremark3");
            /** 合作方自定义备注4 */
            String dremark4 = req.getParameter("dremark4");
            /** 合作方自定义备注5 */
            String dremark5 = req.getParameter("dremark5");
            /** 合作方自定义备注6 */
            String dremark6 = req.getParameter("dremark6");
            /** 业务标示:A00普通收款;B00收款方收款成功后，再冻结资金 */
            String trsflag = "A00";
            //加载配置文件信息
            FileConfigure.getFileConfigure(cltacc_subno);
            // 2. 实例化交易对象
            TrdT2031Request trdRequest = new TrdT2031Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setSrl_ptnsrl(srl_ptnsrl);
            trdRequest.setCltacc_subno(cltacc_subno);
            trdRequest.setCltacc_cltnm(cltacc_cltnm);
            trdRequest.setBillinfo_aclamt(billinfo_aclamt);
            trdRequest.setBillinfo_subject(billinfo_subject);
            trdRequest.setBillinfo_ccycd("CNY");
            trdRequest.setBillinfo_goodsdesc(billinfo_goodsdesc);
            trdRequest.setBillinfo_paytype(billinfo_paytype);
            trdRequest.setBillinfo_secpaytype(billinfo_secpaytype);
            trdRequest.setBillinfo_minitag(billinfo_minitag);
            trdRequest.setNotificationurl(notificationurl);
            trdRequest.setServnoticurl(servnoticurl);
            trdRequest.setReqflg(reqflg);
            trdRequest.setUsage(usage);
            trdRequest.setDremark1(dremark1);
            trdRequest.setDremark2(dremark2);
            trdRequest.setDremark3(dremark3);
            trdRequest.setDremark4(dremark4);
            trdRequest.setDremark5(dremark5);
            trdRequest.setDremark6(dremark6);
            trdRequest.setTrsflag(trsflag);
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
            TrdT2031Response trdResponse = new TrdT2031Response(respMsg);
            logger.info("响应报文[" + trdResponse.getResponsePlainText() + "]");
            //判断响应报文的处理信息
            response = judgeResponse(trdRequest, trdResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 处理返回报文信息处理
     *
     * @param trdRequest
     * @param trdResponse
     * @return
     * @throws UnsupportedEncodingException
     */
    private BaseResponse judgeResponse(TrdT2031Request trdRequest, TrdT2031Response trdResponse) throws UnsupportedEncodingException {
        BaseResponse baseResponse = new BaseResponse();
        String billinfo_paytype = trdRequest.getBillinfo_paytype();
        String billinfo_kjsmsflg = trdRequest.getBillinfo_kjsmsflg();
        // 支付方式： 2：网银 9：银联无卡支付 交易成功 000000
        if ("000000".equals(trdResponse.getMsghd_rspcode())) {
            logger.info("[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]");  // 返回信息
            logger.info("[srl_ptnsrl]=[" + trdResponse.getSrl_ptnsrl() + "]"); // 合作方流水号
            logger.info("[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]");  // 平台流水号
            if ("6".equals(trdRequest.getBillinfo_paytype())) {
                logger.info("[url]=[" + trdResponse.getUrl() + "]");  // PayType=6时为二维码的CODE地址
                logger.info("[imageurl]=[" + trdResponse.getImageurl() + "]"); // PayType=6时返回二维码图片地址
                //添加数据到入金支付数据库中
                addEntryExitAccount(trdRequest, trdResponse);
                //返回成功数据信息给前端页面
                baseResponse = reternData(trdResponse);
            } else if ("2".equals(billinfo_paytype) || "9".equals(billinfo_paytype)) {
                String url = URLDecoder.decode(trdResponse.getUrl(), "UTF-8");
                String strs[] = url.split("\\?", 2);
                String actionUrl = strs[0];
                String params[] = strs[1].split("\\&", 2);
                String kvl1[] = params[0].split("\\=", 2);
                String key1 = kvl1[0];
                String val1 = kvl1[1];
                String kvl2[] = params[1].split("\\=", 2);
                String key2 = kvl2[0];
                String val2 = kvl2[1];
            } else if ("5".equals(billinfo_paytype) && "2".equals(billinfo_kjsmsflg)) {
                logger.info("[state]=[" + trdResponse.getState() + "]"); // PayType=5且KJSMSFlg=2时返回交易结果::1成功;2失败;3处理中
            } else if ("8".equals(billinfo_paytype) || "A".equals(billinfo_paytype)) {
                logger.info("[authcode]=[" + trdResponse.getAuthcode() + "]");  // PayType=8/A时返回授权码
            } else if ("5".equals(billinfo_paytype)) {
                logger.info("[H5支付(云闪付支付)]=[" + trdResponse.getAuthcode() + "]");  // H5支付(云闪付支付)时返回授权码
                //添加数据到入金支付数据库中
                addEntryExitAccount(trdRequest, trdResponse);
                //返回成功数据信息给前端页面
                baseResponse = reternData(trdResponse);
            }
        } else {
            //添加数据到入金支付数据库中
            addEntryExitAccount(trdRequest, trdResponse);
            baseResponse.setCode(trdResponse.getMsghd_rspcode());
            baseResponse.setMsg(trdResponse.getMsghd_rspmsg());
        }
        return baseResponse;
    }

    /**
     * 返回成功数据信息给前端页面
     *
     * @param trdResponse
     * @return
     */
    private BaseResponse reternData(TrdT2031Response trdResponse) {
        Map<String,String> map = new TreeMap<>();
        map.put("url", trdResponse.getUrl());
        BaseResponse response = new BaseResponse();
        response.setCode(trdResponse.getMsghd_rspcode());
        response.setMsg(trdResponse.getMsghd_rspmsg());
        response.setData(JSONObject.fromObject(map));

        return response;
    }

    /**
     * 添加数据到入金支付数据中
     *
     * @param trdRequest
     * @param trdResponse
     */
    private void addEntryExitAccount(TrdT2031Request trdRequest, TrdT2031Response trdResponse) {
        EntryExitAccount account = new EntryExitAccount();
        account.setClientStatus("0");
        account.setSecPayType(trdRequest.getBillinfo_secpaytype());
        //根据资金账户查询到对应的用户id以及用户Account的id
        String cltacc_subno = trdRequest.getCltacc_subno();
        UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
        account.setUserId(userAccount.getUserId());
        account.setUserAccountId(userAccount.getId());
        account.setUsage(trdRequest.getUsage());
        account.setUrl(trdResponse.getUrl());
        account.setImageUrl(trdResponse.getImageurl());
        account.setSubject(trdRequest.getBillinfo_subject());
        account.setStatus("0");
        account.setServnoticeUrl(null);
//        account.setSendToClientTimes();
        account.setReqFlg(trdRequest.getReqflg());
        account.setPtnSrl(trdResponse.getSrl_ptnsrl());
        account.setPlatSrl(trdResponse.getSrl_platsrl());
        account.setPayType(trdRequest.getBillinfo_paytype());
        account.setSecPayType(trdRequest.getBillinfo_secpaytype());
        account.setNotificationUrl(null);
        account.setMoney(Long.valueOf(trdRequest.getBillinfo_aclamt()));
        account.setGoodsDesc(trdRequest.getBillinfo_goodsdesc());
        account.setDate(DateUtils.toDate(new Date()));
        insert(account);
    }

    /**
     * 新增入金支付-集成交易对象信息
     *
     * @param account
     * @return
     */
    public int insert(EntryExitAccount account) {
        account.setOrderNo(GenerateOrderNoUtil.gens("eea", 530L));
        account.setCreatedAt(DateUtils.toTimestamp()); //创建时间
        return mapper.insert(account);
    }

    /**
     * 根据根据客户方交易流水号判断该交易流水号是否存在
     *
     * @param ptnSrl 客户方交易流水号
     * @return
     */
    public EntryExitAccount findByPtnSrl(String ptnSrl) {
        return mapper.findByPtnSrl(ptnSrl);
    }

    /**
     * 支付直通车,异步交易通知地址信息
     *
     * @return
     */
    public Map<String, String> orderScantoPay(HttpServletRequest req, HttpServletResponse resp) {
        NoticeRequest noticeRequest = null;
        NtcBaseResponse noticeResponse = null;
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            // 1 获得参数ptncode, trdcode, message和signature
            String ptncode = req.getParameter("ptncode");
            String trdcode = req.getParameter("trdcode");
            String message = req.getParameter("message");
            String signature = req.getParameter("signature");
            logger.info("ptncode=" + ptncode);
            logger.info("trdcode=" + trdcode);
            logger.info("message=" + message);
            logger.info("signature=" + signature);
            if (StringUtil.isEmpty(ptncode) || StringUtil.isEmpty(trdcode) || StringUtil.isEmpty(message) || StringUtil.isEmpty(signature)) {
                throw new CodeException("SDER04", "参数错误");
            }
            // 2 生成交易请求对象(验签)
            noticeRequest = new NoticeRequest(message, signature);
            logger.info("通知报文: " + noticeRequest.getPlainText());
            // 3 业务处理
            if ("K5129".equals(noticeRequest.getMsghd_trcd())) {
                // 3.3.5 授信结果通知[K5129]
                NtcK5129Request nr = new NtcK5129Request(noticeRequest.getDocument());
                // ！！！ 在这里添加合作方处理逻辑！！！
                logger.info("srl_ptnsrl=" + nr.getSrl_ptnsrl());
                logger.info("clt_cltnm=" + nr.getClt_cltnm());
                logger.info("clt_kind=" + nr.getClt_kind());
                logger.info("clt_cdno=" + nr.getClt_cdno());
                logger.info("clt_orgcd=" + nr.getClt_orgcd());
                logger.info("credit_crdorg=" + nr.getCredit().getCrdorg());
                logger.info("credit_crdno=" + nr.getCredit().getCrdno());
                logger.info("credit_crecd=" + nr.getCredit().getCrecd());
                logger.info("credit_lnrt=" + nr.getCredit().getLnrt());
                logger.info("credit_amt=" + nr.getCredit().getAmt());
                logger.info("credit_stdt=" + nr.getCredit().getStdt());
                logger.info("credit_eddt=" + nr.getCredit().getEddt());
                logger.info("credit_sgperiod=" + nr.getCredit().getSgperiod());
                logger.info("credit_sgperiod=" + nr.getCredit().getSgperiodunit());
                logger.info("credit_ccycd=" + nr.getCredit().getCcycd());
                logger.info("credit_loanuse=" + nr.getCredit().getLoanuse());
            } else if ("K5224".equals(noticeRequest.getMsghd_trcd())) {
                // 放款结果通知[T3009]
                NtcK5224Request nr = new NtcK5224Request(noticeRequest.getDocument());
                // ！！！ 在这里添加合作方处理逻辑！！！
                logger.info("srl_ptnsrl=" + nr.getSrl_ptnsrl());
                logger.info("clt_cltnm=" + nr.getClt_cltnm());
                logger.info("clt_kind=" + nr.getClt_kind());
                logger.info("clt_cdno=" + nr.getClt_cdno());
                logger.info("clt_orgcd=" + nr.getClt_orgcd());
                logger.info("loan_loanno=" + nr.getLoan_loanno());
                logger.info("loan_state=" + nr.getLoan_state());
                logger.info("loan_opion=" + nr.getLoan_opion());
                logger.info("loan_lnamt=" + nr.getLoan_lnamt());
                logger.info("loan_lndt=" + nr.getLoan_lndt());
                logger.info("loan_lneddt=" + nr.getLoan_lneddt());
                List<BusiBill> busiBillList = nr.getBusiBillList();
                if (null != busiBillList && !busiBillList.isEmpty()) {
                    for (BusiBill busiBill : busiBillList) {
                        logger.info("[busitype]=[" + busiBill.getBusitype() + "]");
                        logger.info("[billno]=[" + busiBill.getBillno() + "]");
                    }
                }
                List<Plan> planList = nr.getPlanList();
                if (null != planList && !planList.isEmpty()) {
                    for (Plan plan : planList) {
                        logger.info("[mark]=[" + plan.getMark() + "]");
                        logger.info("[days]=[" + plan.getDays() + "]");
                        logger.info("[start]=[" + plan.getStart() + "]");
                        logger.info("[end]=[" + plan.getEnd() + "]");
                        logger.info("[duedate]=[" + plan.getDuedate() + "]");
                        logger.info("[principal]=[" + plan.getPrincipal() + "]");
                        logger.info("[interest]=[" + plan.getInterest() + "]");
                        logger.info("[bqyhjine]=[" + plan.getBqyhjine() + "]");
                        logger.info("[bqyhfuli]=[" + plan.getBqyhfuli() + "]");
                        logger.info("[bqyjnfwf]=[" + plan.getBqyjnfwf() + "]");
                        logger.info("[yuqifeil]=[" + plan.getYuqifeil() + "]");
                        logger.info("[yuqijnje]=[" + plan.getYuqijnje() + "]");
                        logger.info("[bqshjine]=[" + plan.getBqshjine() + "]");
                        logger.info("[bqhkshij]=[" + plan.getBqhkshij() + "]");
                        logger.info("[bqhkzhta]=[" + plan.getBqhkzhta() + "]");
                        logger.info("[yuqzhtai]=[" + plan.getYuqzhtai() + "]");
                        logger.info("[yuqitash]=[" + plan.getYuqitash() + "]");
                    }
                }
            } else if ("K5242".equals(noticeRequest.getMsghd_trcd())) {
                // 3.6.6 还款结果通知[K5242]
                NtcK5242Request nr = new NtcK5242Request(noticeRequest.getDocument());
                // ！！！ 在这里添加合作方处理逻辑！！！
                logger.info("srl_ptnsrl=" + nr.getSrl_ptnsrl());
                logger.info("loan_loanno=" + nr.getLoan_loanno()); // 借款编号
                logger.info("loan_hkno=" + nr.getLoan_hkno()); // 还款申请编号
                logger.info("loan_rstflg=" + nr.getLoan_rstflg()); // 还款结果(1：还款成功、2：还款失败)
                logger.info("loan_crhkbj=" + nr.getLoan_crhkbj()); // 本次还本金额
                logger.info("loan_crhklx=" + nr.getLoan_crhklx()); // 本次还息金额
                logger.info("loan_lnbal=" + nr.getLoan_lnbal()); // 本次还款后的该笔借款余额
                if ("2".equals(nr.getLoan_rstflg())) { // 还款结果(1：还款成功、2：还款失败)
                    logger.info("loan_accrual=" + nr.getLoan_opion()); // 失败原因
                }
                List<Plan> planList = nr.getPlanList();
                if (null != planList && !planList.isEmpty()) {
                    for (Plan plan : planList) {
                        logger.info("[mark]=[" + plan.getMark() + "]");
                        logger.info("[days]=[" + plan.getDays() + "]");
                        logger.info("[start]=[" + plan.getStart() + "]");
                        logger.info("[end]=[" + plan.getEnd() + "]");
                        logger.info("[duedate]=[" + plan.getDuedate() + "]");
                        logger.info("[principal]=[" + plan.getPrincipal() + "]");
                        logger.info("[interest]=[" + plan.getInterest() + "]");
                        logger.info("[bqyhjine]=[" + plan.getBqyhjine() + "]");
                        logger.info("[bqyhfuli]=[" + plan.getBqyhfuli() + "]");
                        logger.info("[bqyjnfwf]=[" + plan.getBqyjnfwf() + "]");
                        logger.info("[yuqifeil]=[" + plan.getYuqifeil() + "]");
                        logger.info("[yuqijnje]=[" + plan.getYuqijnje() + "]");
                        logger.info("[bqshjine]=[" + plan.getBqshjine() + "]");
                        logger.info("[bqhkshij]=[" + plan.getBqhkshij() + "]");
                        logger.info("[bqhkzhta]=[" + plan.getBqhkzhta() + "]");
                        logger.info("[yuqzhtai]=[" + plan.getYuqzhtai() + "]");
                        logger.info("[yuqitash]=[" + plan.getYuqitash() + "]");
                    }
                }
            } else if ("K5246".equals(noticeRequest.getMsghd_trcd())) {
                // 3.5.11 发货结果通知
                NtcK5246Request nr = new NtcK5246Request(noticeRequest.getDocument());
                // ！！！ 在这里添加合作方处理逻辑！！！
                logger.info("srl_ptnsrl=" + nr.getSrl_ptnsrl());
                logger.info("loan_loanno=" + nr.getLoan_loanno()); // 借款编号
                logger.info("loan_fghno=" + nr.getLoan_fghno()); // 发货通知编号
                logger.info("loan_crhkbj=" + nr.getLoan_crhkbj()); // 本次发货价值
                logger.info("loan_lnbal=" + nr.getLoan_lnbal()); // 本次发货后剩余的未发货值
            } else {
                throw new CodeException("MK2002", "暂不支持此交易");
            }
            // 4 处理响应报文参数
            noticeResponse = new NoticeResponse(noticeRequest);
            noticeResponse.setSrl_ptnsrl("" + System.currentTimeMillis()); // 合作方业务流水号
            // 000000 成功; 其它 失败
            noticeResponse.setMsghd_rspcode("000000");
            noticeResponse.setMsghd_rspmsg("业务办理成功");
        } catch (CodeException e) {
            // 4 处理响应报文参数
            noticeResponse = new NoticeResponse(noticeRequest);
            noticeResponse.setSrl_ptnsrl("" + System.currentTimeMillis()); // 合作方业务流水号
            // 000000 成功; 其它 失败
            noticeResponse.setMsghd_rspcode(e.getCode());
            noticeResponse.setMsghd_rspmsg(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // 4 处理响应报文参数
            noticeResponse = new NoticeResponse(noticeRequest);
            noticeResponse.setSrl_ptnsrl("" + System.currentTimeMillis()); // 合作方业务流水号
            // 000000 成功; 其它 失败
            noticeResponse.setMsghd_rspcode("ERRRRR");
            noticeResponse.setMsghd_rspmsg("业务办理失败");
            e.printStackTrace();
        } finally {
            // 5 响应智融平台
            PrintWriter out = null;
            try {
                if (null == noticeResponse) {
                    noticeResponse = new NoticeResponse(noticeRequest);
                    noticeResponse.setSrl_ptnsrl("" + System.currentTimeMillis()); // 合作方业务流水号
                    // 000000 成功; 其它 失败
                    noticeResponse.setMsghd_rspcode("ERRRRR");
                    noticeResponse.setMsghd_rspmsg("交易结果未知");
                }
                noticeResponse.process();
                out = resp.getWriter();
                logger.info("响应报文: " + noticeResponse.getPlainText());
                String msg = noticeResponse.getMessage();
                out.print(msg);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != out) {
                    out.close();
                }
            }
        }
        return null;
    }
}
