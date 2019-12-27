package com.example.asc.asc.trd.asc.payType.service.impl;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.payType.service.PayTypeService;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.*;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT2031Request;
import com.trz.netwk.api.trd.TrdT2031Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 支付方式支付类型
 *
 * @author lujunjie
 * @date   2018/02/16
 */
@Service()
public class PayTypeServiceImpl implements PayTypeService {

    private static final Logger logger = LoggerFactory.getLogger(PayTypeServiceImpl.class);
    private static final String TAG = "{入金支付}-";

    private EntryExitAccountService service;
    private UserAccountService userAccountService;

    @Autowired
    public void setService(EntryExitAccountService service) {
        this.service = service;
    }
    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 生成支付二维码URL
     * @param totalFee 标价金额(分单位)
     * @param payType 支付类型
     * @throws Exception
     */
    @Override
    public String payUrl(Double totalFee, String payType) throws Exception {
        String payUrl = null;
        try {
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat("");
            /** 合作方交易流水号 */
            String ptnsrl = getPtnSrl(msghd_trdt);
            String srl_ptnsrl = GenerateOrderNoUtil.gens("eea",530L);
            /** 资金账号 */
            String cltacc_subno = "1931115000186036";
            UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
            /** 户名 */
            String cltacc_cltnm = userAccount.getName();
            /** 支付金额 */
            String billinfo_aclamt = MoneyUtils.changeY2F(totalFee.toString());
            /** 支付方式-二级分类(1：企业网银PayType=2必输;2：个人网银PayType=2必输;3：支付宝PayType=6/8/A必输;4：微信PayType=6/8/A必输；5：银联PayType=6必输) */
            String billinfo_secpaytype = payType;
            /** 支付方式：2：网银;5：快捷支付;6：正扫支付;8：公众号支付;9：银联无卡支付;A：手机APP跳转支付 */
            Map<String, String> stringMap = service.getPayType(billinfo_secpaytype);
            String billinfo_paytype = stringMap.get("billinfo_secpaytype");
            if(billinfo_paytype.equals("H")){
                billinfo_secpaytype = "5";
            }
            /** 订单标题:PayType=6/8/A时必输 */
            String billinfo_subject = stringMap.get("subject");  //商品主题描述
            /** 商品描述:PayType=6/8/A时必输 */
            String billinfo_goodsdesc = stringMap.get("goodsDesc");  //商品描述
            /** 是否小程序支付 0 不是 1 是 */
            String billinfo_minitag = "0";
            /** 发送端标记:0手机;1PC端 */
            String reqflg = "1";
            /** 页面通知URL */
            String notificationurl = "";
            /** 后台通知URL-若不传值则默认按照后台配置的地址进行通知交易结果 */
            String servnoticurl = "";
            /** 资金用途(附言) */
            String usage = "H5支付";
            /** 合作方自定义备注1 */
            String dremark1 = "合作方自定义备注1";
            /** 合作方自定义备注2 */
            String dremark2 = "合作方自定义备注2";
            /** 合作方自定义备注3 */
            String dremark3 = "合作方自定义备注3";
            /** 合作方自定义备注4 */
            String dremark4 = "合作方自定义备注4";
            /** 合作方自定义备注5 */
            String dremark5 = "合作方自定义备注5";
            /** 合作方自定义备注6 */
            String dremark6 = "合作方自定义备注6";
            /** 业务标示:A00普通收款;B00收款方收款成功后，再冻结资金 */
            String trsflag = "A00";
            EntryExitAccount account = new EntryExitAccount();
            account.setSecPayType(billinfo_secpaytype);
            //根据资金账户查询到对应的用户id以及用户Account的id
            account.setUserId(userAccount.getUserId());
            account.setUserAccountId(userAccount.getId());
            account.setUsage(usage);
            account.setSubject(billinfo_subject);
            account.setStatus("0");
            account.setServnoticeUrl(servnoticurl);
            account.setSendToClientTimes(0);
            account.setReqFlg(reqflg);
            account.setPtnSrl(ptnsrl);
            account.setOrderNo(srl_ptnsrl);
            account.setPayType(billinfo_paytype);
            account.setNotificationUrl(notificationurl);
            account.setMoney(Long.valueOf(billinfo_aclamt));
            account.setGoodsDesc(billinfo_goodsdesc);
            account.setDate(DateUtils.toStringDate(new Date()));
            service.insert(account);
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
            logger.info(TAG + "请求报文[" + trdRequest.getRequestPlainText() + "]");
            logger.info(TAG + "签名原文[" + trdRequest.getRequestMessage() + "]");
            logger.info(TAG + "签名数据[" + trdRequest.getRequestSignature() + "]");
            // 4. 与融资平台通信
            TrdMessenger trdMessenger = new TrdMessenger();
            // message
            String respMsg = trdMessenger.send(trdRequest);
            // 5. 处理交易结果
            TrdT2031Response trdResponse = new TrdT2031Response(respMsg);
            logger.info(TAG + "响应报文[" + trdResponse.getResponsePlainText() + "]");
            //判断响应报文的处理信息
            payUrl = judgeResponse(trdRequest, trdResponse, notificationurl, servnoticurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payUrl;
    }

    /**
     * 处理返回报文信息处理
     *
     * @param trdRequest
     * @param trdResponse
     * @param servnoticurl
     * @param notificationurl
     * @return
     * @throws UnsupportedEncodingException
     */
    private String judgeResponse(TrdT2031Request trdRequest, TrdT2031Response trdResponse, String notificationurl, String servnoticurl) throws UnsupportedEncodingException {
        String billinfo_paytype = trdRequest.getBillinfo_paytype();
        String billinfo_kjsmsflg = trdRequest.getBillinfo_kjsmsflg();
        // 支付方式： 2：网银 9：银联无卡支付 交易成功 000000
        if ("000000".equals(trdResponse.getMsghd_rspcode())) {
            logger.info(TAG + "[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]");  // 返回信息
            logger.info(TAG + "[srl_ptnsrl]=[" + trdResponse.getSrl_ptnsrl() + "]"); // 合作方流水号
            logger.info(TAG + "[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]");  // 平台流水号
            if ("6".equals(trdRequest.getBillinfo_paytype())) {
                logger.info(TAG + "[url]=[" + trdResponse.getUrl() + "]");  // PayType=6时为二维码的CODE地址
                logger.info(TAG + "[imageurl]=[" + trdResponse.getImageurl() + "]"); // PayType=6时返回二维码图片地址
                //根据合作方交易流水号查到对应得订单信息
                EntryExitAccount account = service.findByOrderNo(trdResponse.getSrl_ptnsrl());
                account.setStatus("0");
                account.setPlatSrl(trdResponse.getSrl_platsrl());
                account.setUrl(trdResponse.getUrl());
                account.setImageUrl(trdResponse.getImageurl());
                service.update(account.getId(), account);
                return trdResponse.getUrl();
            } else if ("H".equals(billinfo_paytype)) {
                logger.info(TAG + "[H5支付(云闪付支付)]=[" + trdResponse.getAuthcode() + "]");  // H5支付(云闪付支付)时返回授权码
                //根据合作方交易流水号查到对应得订单信息
                EntryExitAccount account = service.findByOrderNo(trdResponse.getSrl_ptnsrl());
                account.setStatus("0");
                account.setPlatSrl(trdResponse.getSrl_platsrl());
                service.update(account.getId(), account);
                Map<String, String> map = new TreeMap<>();
                //解析code的内容信息
                TreeMap<String, Object> treeMap = JsoupHtmlUtils.getJsoupHtmlUtils(Base64.getFromBase64(trdResponse.getAuthcode()));
                //将数据添加到云闪付数据库中
                Long id = service.addCloudFlashoverOrder(treeMap, trdResponse);
                return  "http://39.107.40.13:8080/entry-exit-account/unifiedOrder/" + id;
            }
        } else {
            EntryExitAccount account = service.findByOrderNo(trdResponse.getSrl_ptnsrl());
            account.setStatus("2");
            account.setPlatSrl(trdResponse.getSrl_platsrl());
            service.update(account.getId(), account);
        }
        return null;
    }

    /**
     * 合作方交易流水号
     *
     * @param msghd_trdt 交易日期
     * @return
     */
    private String getPtnSrl(String msghd_trdt) {
        String srl_ptnsrl = msghd_trdt + String.valueOf(System.currentTimeMillis());
        if (service.findByPtnSrl(srl_ptnsrl) != null) {
            getPtnSrl(msghd_trdt);
        }
        return srl_ptnsrl;
    }
}
