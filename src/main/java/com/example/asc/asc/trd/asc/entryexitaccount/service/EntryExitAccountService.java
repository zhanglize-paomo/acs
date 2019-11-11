package com.example.asc.asc.trd.asc.entryexitaccount.service;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.mapper.EntryExitAccountMapper;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.DateUtils;
import com.example.asc.asc.util.GenerateOrderNoUtil;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT2031Request;
import com.trz.netwk.api.trd.TrdT2031Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
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

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    private EntryExitAccountMapper mapper;
    @Autowired
    public void setMapper(EntryExitAccountMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * H5支付,调用云闪付的平台进行支付
     *
     * @return
     */
    public Map<String, String> scantoPay(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> treeMap = new TreeMap<>();
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghd_trdt"));
            /** 合作方交易流水号 */
            String srl_ptnsrl = req.getParameter("ptnsrl");
            /** 资金账号 */
            String cltacc_subno = req.getParameter("subno");
            /** 户名 */
            String cltacc_cltnm = req.getParameter("cltacc_cltnm");
            /** 支付金额 */
            String billinfo_aclamt = req.getParameter("billinfo_aclamt");
            /** 支付方式：2：网银;5：快捷支付;6：正扫支付;8：公众号支付;9：银联无卡支付;A：手机APP跳转支付 */
            String billinfo_paytype = "H";
            /** 支付方式-二级分类(1：企业网银PayType=2必输;2：个人网银PayType=2必输;3：支付宝PayType=6/8/A必输;4：微信PayType=6/8/A必输；5：银联PayType=6必输) */
            String billinfo_secpaytype = "5";
            /** 订单标题:PayType=6/8/A时必输 */
            String billinfo_subject = req.getParameter("billinfo_subject");
            /** 商品描述:PayType=6/8/A时必输 */
            String billinfo_goodsdesc = req.getParameter("billinfo_goodsdesc");
            /** 是否小程序支付 0 不是 1 是 */
            String billinfo_minitag = "0";
            /** 发送端标记:0手机;1PC端 */
            String reqflg = "1";
            /** 页面通知URL */
            String notificationurl = req.getParameter("notificationurl");
            /** 后台通知URL-若不传值则默认按照后台配置的地址进行通知交易结果 */
            String servnoticurl = req.getParameter("servnoticurl");
            /** 资金用途(附言) */
            String usage = req.getParameter("usage");
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
            treeMap = judgeResponse(trdRequest, trdResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeMap;
    }

    /**
     * 处理返回报文信息处理
     *
     * @param trdRequest
     * @param trdResponse
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, String> judgeResponse(TrdT2031Request trdRequest, TrdT2031Response trdResponse) throws UnsupportedEncodingException {
        Map<String, String> treeMap = new TreeMap<>();
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
                treeMap = reternData(trdResponse);
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
            }else if("5".equals(billinfo_paytype)){
                logger.info("[H5支付(云闪付支付)]=[" + trdResponse.getAuthcode() + "]");  // H5支付(云闪付支付)时返回授权码
                //添加数据到入金支付数据库中
                addEntryExitAccount(trdRequest, trdResponse);
                //返回成功数据信息给前端页面
                treeMap = reternData(trdResponse);
            }
        } else {
            //添加数据到入金支付数据库中
            addEntryExitAccount(trdRequest, trdResponse);
            treeMap.put("code", trdResponse.getMsghd_rspcode());
            treeMap.put("msg", trdResponse.getMsghd_rspmsg());
        }
        return treeMap;
    }

    /**
     * 返回成功数据信息给前端页面
     *
     * @param trdResponse
     * @return
     */
    private Map<String, String> reternData(TrdT2031Response trdResponse) {
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("code", trdResponse.getMsghd_rspcode());
        treeMap.put("url", trdResponse.getUrl());
        treeMap.put("msg", trdResponse.getMsghd_rspmsg());
        return treeMap;
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
        account.setOrderNo(GenerateOrderNoUtil.gens("eea",530L));
        account.setCreatedAt(DateUtils.toTimestamp()); //创建时间
        return mapper.insert(account);
    }

    /**
     * 根据根据客户方交易流水号判断该交易流水号是否存在
     *
     * @param ptnSrl  客户方交易流水号
     * @return
     */
    public EntryExitAccount findByPtnSrl(String ptnSrl) {
        return mapper.findByPtnSrl(ptnSrl);
    }
}
