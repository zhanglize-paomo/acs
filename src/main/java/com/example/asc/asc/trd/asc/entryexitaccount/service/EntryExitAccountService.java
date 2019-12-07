package com.example.asc.asc.trd.asc.entryexitaccount.service;

import com.blue.util.StringUtil;
import com.example.asc.asc.trd.asc.cloudflashoverorder.domain.CloudFlashoverOrder;
import com.example.asc.asc.trd.asc.cloudflashoverorder.service.CloudFlashoverOrderService;
import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.mapper.EntryExitAccountMapper;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.asc.users.domain.Users;
import com.example.asc.asc.trd.asc.users.service.UsersService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.Base64;
import com.example.asc.asc.util.*;
import com.trz.netwk.api.ntc.NoticeRequest;
import com.trz.netwk.api.ntc.NoticeResponse;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT2031Request;
import com.trz.netwk.api.trd.TrdT2031Response;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 入金直通车_异步交易[T2031]业务层
 *
 * @author zhanglize
 * @create 2019/11/8
 */
@Service
public class EntryExitAccountService {

    private static final Logger logger = LoggerFactory.getLogger(EntryExitAccountService.class);
    private static final String TAG = "{入金支付}-";
    private static final String TAG_ = "{入金支付异步}-";
    private UserAccountService userAccountService;
    private UsersService usersService;
    private CloudFlashoverOrderService cloudFlashoverOrderService;
    private EntryExitAccountMapper mapper;

    @Autowired
    public void setCloudFlashoverOrderService(CloudFlashoverOrderService cloudFlashoverOrderService) {
        this.cloudFlashoverOrderService = cloudFlashoverOrderService;
    }

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Autowired
    public void setMapper(EntryExitAccountMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * 以post方式调用对方接口方法
     *
     * @param pathUrl           请求地址信息
     * @param data              数据信息
     * @param num               数量
     * @param sendToClientTimes 发送次数
     * @param account           订单号信息
     */
    public String doPostOrGet(String pathUrl, TreeMap<String, Object> data, int num, int sendToClientTimes, EntryExitAccount account) {
        String str = HttpUtil2.doPost(pathUrl, data, "utf-8");
        sendMessage(str, pathUrl, data, num, sendToClientTimes, account);
        return str;
    }


    public String checkDigest(String appid,TreeMap<String, Object> treeMap) {
        String digest = null;
        try {
            String sortvalue = usersService.findAppId(appid).getSecret();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                sortvalue += entry.getValue().toString().trim();
            }
            digest = Base64.getBase64(
                    SecuritySHA1Utils.shaEncode(
                            appid +
                                    SecuritySHA1Utils.shaEncode(sortvalue).toUpperCase())
                            .toUpperCase()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;

    }


    /**
     * 判断下游消息是否为空,如果为空,每隔5秒发送一次请求,
     * 发送4次请求消息,总共估计20秒
     *
     * @param str               下游消息信息
     * @param pathUrl           请求地址信息
     * @param data              数据信息
     * @param num               次数
     * @param sendToClientTimes
     * @param account
     */
    private void sendMessage(String str, String pathUrl, TreeMap<String, Object> data, int num, int sendToClientTimes, EntryExitAccount account) {
        if (StringUtil.isEmpty(str)) {
            if (StringUtils.isEmpty(sendToClientTimes)) {
                //默认发送给下游客户四次请求
                if (num != 4) {
                    num += 1;
                    try {
                        Thread.sleep(5000);
                        doPostOrGet(pathUrl, data, num, sendToClientTimes, account);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (num != sendToClientTimes) {
                    num += 1;
                    try {
                        Thread.sleep(5000);
                        doPostOrGet(pathUrl, data, num, sendToClientTimes, account);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            account.setClientStatus("1");
            update(account.getId(), account);
        }
    }


    /**
     * 支付,调用云闪付的平台进行支付
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
            String srl_ptnsrl = req.getParameter("ptnSrl");
            /** 资金账号 */
            String cltacc_subno = req.getParameter("subNo");
            UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
            /** 户名 */
            String cltacc_cltnm = userAccount.getName();
            /** 支付金额 */
            String billinfo_aclamt = req.getParameter("money");
            /** 支付方式-二级分类(1：企业网银PayType=2必输;2：个人网银PayType=2必输;3：支付宝PayType=6/8/A必输;4：微信PayType=6/8/A必输；5：银联PayType=6必输) */
            String billinfo_secpaytype = req.getParameter("payType");
            /** 支付方式：2：网银;5：快捷支付;6：正扫支付;8：公众号支付;9：银联无卡支付;A：手机APP跳转支付 */
            Map<String, String> stringMap = getPayType(billinfo_secpaytype);
            String billinfo_paytype = stringMap.get("billinfo_secpaytype");
            if (StringUtils.isEmpty(billinfo_paytype)) {
                response.setCode("ZF310");
                response.setMsg("支付方式不存在");
                response.setData(null);
                return response;
            }
            if (billinfo_secpaytype.equals("6")) {
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
            String notificationurl = req.getParameter("notificationurl");
            /** 后台通知URL-若不传值则默认按照后台配置的地址进行通知交易结果 */
            String servnoticurl = req.getParameter("servNoticeUrl");
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
//            trdRequest.setNotificationurl(notificationurl);
//            trdRequest.setServnoticurl(servnoticurl);
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
            response = judgeResponse(trdRequest, trdResponse, notificationurl, servnoticurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 根据二级支付方式获取到一级支付方式
     *
     * @param billinfo_secpaytype
     * @return
     */
    private Map<String, String> getPayType(String billinfo_secpaytype) {
        Map<String, String> map = new HashMap<>();
        if (billinfo_secpaytype.equals("3")) {  //支付宝
            map.put("billinfo_secpaytype", "6");
            map.put("subject", "支付宝");
            map.put("goodsDesc", "支付宝");
            return map;
        } else if (billinfo_secpaytype.equals("4")) {  //微信
            map.put("billinfo_secpaytype", "6");
            map.put("subject", "微信");
            map.put("goodsDesc", "微信");
            return map;
        } else if (billinfo_secpaytype.equals("5")) {  //银联
            map.put("billinfo_secpaytype", "6");
            map.put("subject", "银联");
            map.put("goodsDesc", "银联");
            return map;
        } else if (billinfo_secpaytype.equals("6")) {  //H5支付
            map.put("billinfo_secpaytype", "H");
            map.put("subject", "云闪付");
            map.put("goodsDesc", "云闪付");
            return map;
        }
        return null;
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
    private BaseResponse judgeResponse(TrdT2031Request trdRequest, TrdT2031Response trdResponse, String notificationurl, String servnoticurl) throws UnsupportedEncodingException {
        BaseResponse baseResponse = new BaseResponse();
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
                //添加数据到入金支付数据库中
                addEntryExitAccount(trdRequest, trdResponse, notificationurl, servnoticurl);
                //返回成功数据信息给前端页面
                baseResponse = reternData(trdResponse, billinfo_paytype);
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
                logger.info(TAG + "[state]=[" + trdResponse.getState() + "]"); // PayType=5且KJSMSFlg=2时返回交易结果::1成功;2失败;3处理中
            } else if ("8".equals(billinfo_paytype) || "A".equals(billinfo_paytype)) {
                logger.info("[authcode]=[" + trdResponse.getAuthcode() + "]");  // PayType=8/A时返回授权码
            } else if ("H".equals(billinfo_paytype)) {
                logger.info(TAG + "[H5支付(云闪付支付)]=[" + trdResponse.getAuthcode() + "]");  // H5支付(云闪付支付)时返回授权码
                //添加数据到入金支付数据库中
                addEntryExitAccount(trdRequest, trdResponse, notificationurl, servnoticurl);
                //返回成功数据信息给前端页面
                baseResponse = reternData(trdResponse, billinfo_paytype);
            }
        } else {
            //添加数据到入金支付数据库中
            addEntryExitAccount(trdRequest, trdResponse, notificationurl, servnoticurl);
            baseResponse.setCode(trdResponse.getMsghd_rspcode());
            baseResponse.setMsg(trdResponse.getMsghd_rspmsg());
        }
        return baseResponse;
    }

    /**
     * 返回成功数据信息给前端页面
     *
     * @param trdResponse
     * @param billinfo_paytype
     * @return
     */
    private BaseResponse reternData(TrdT2031Response trdResponse, String billinfo_paytype) {
        BaseResponse response = new BaseResponse();
        if (billinfo_paytype.equals("H")) {
            Map<String, String> map = new TreeMap<>();
            //解析code的内容信息
            TreeMap<String, Object> treeMap = JsoupHtmlUtils.getJsoupHtmlUtils(Base64.getFromBase64(trdResponse.getAuthcode()));
            //将数据添加到云闪付数据库中
            Long id = addCloudFlashoverOrder(treeMap, trdResponse);
            String url = "http://39.107.40.13:8080/entry-exit-account/unifiedOrder/" + id;
            map.put("url", url);
            response.setData(JSONObject.fromObject(map));
        } else {
            Map<String, String> map = new TreeMap<>();
            map.put("url", trdResponse.getUrl());
            response.setData(JSONObject.fromObject(map));
        }
        response.setCode(trdResponse.getMsghd_rspcode());
        response.setMsg(trdResponse.getMsghd_rspmsg());
        return response;
    }


    /**
     * 新增云闪付信息
     *
     * @param treeMap
     * @param trdResponse
     * @return
     */
    private Long addCloudFlashoverOrder(TreeMap<String, Object> treeMap, TrdT2031Response trdResponse) {
        CloudFlashoverOrder order = new CloudFlashoverOrder();
        Long id = new SnowflakeIdUtils().nextId();
        order.setId(id);
        order.setAuthCode(trdResponse.getAuthcode());
        order.setAccessType(treeMap.get("accessType").toString());
        order.setBackUrl(treeMap.get("backUrl").toString());
        order.setBizType(treeMap.get("bizType").toString());
        order.setCertId(treeMap.get("certId").toString());
        order.setChannelType(treeMap.get("channelType").toString());
        order.setCurrencyCode(treeMap.get("currencyCode").toString());
        order.setEncoding(treeMap.get("encoding").toString());
        order.setFrontUrl(treeMap.get("frontUrl").toString());
        order.setMerId(treeMap.get("merId").toString());
        order.setOrderId(treeMap.get("orderId").toString());
        order.setPayTimeout(treeMap.get("payTimeout").toString());
        order.setRiskRateInfo(treeMap.get("riskRateInfo").toString());
        order.setSignature(treeMap.get("signature").toString());
        order.setSignMethod(treeMap.get("signMethod").toString());
        order.setTxnAmt(Long.valueOf(treeMap.get("txnAmt").toString()));
        order.setTxnSubType(treeMap.get("txnSubType").toString());
        order.setTxnTime(treeMap.get("txnTime").toString());
        order.setTxnType(treeMap.get("txnType").toString());
        order.setVersion(treeMap.get("version").toString());
        order.setCreateAt(DateUtils.stringToDate());
        cloudFlashoverOrderService.insert(order);
        return id;
    }

    /**
     * 添加数据到入金支付数据中
     *
     * @param trdRequest
     * @param trdResponse
     * @param servnoticurl
     * @param notificationurl
     */
    private void addEntryExitAccount(TrdT2031Request trdRequest, TrdT2031Response trdResponse, String notificationurl, String servnoticurl) {
        EntryExitAccount account = new EntryExitAccount();
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
        if (trdResponse.getMsghd_rspcode().equals("000000")) {
            account.setStatus("0");
        } else {
            account.setStatus("2");
        }
        account.setServnoticeUrl(servnoticurl);
//        account.setSendToClientTimes(0);
        account.setReqFlg(trdRequest.getReqflg());
        account.setPtnSrl(trdResponse.getSrl_ptnsrl());
        account.setPlatSrl(trdResponse.getSrl_platsrl());
        account.setPayType(trdRequest.getBillinfo_paytype());
        account.setSecPayType(trdRequest.getBillinfo_secpaytype());
        account.setNotificationUrl(notificationurl);
        account.setMoney(Long.valueOf(trdRequest.getBillinfo_aclamt()));
        account.setGoodsDesc(trdRequest.getBillinfo_goodsdesc());
        account.setDate(new Date());
        insert(account);
    }

    /**
     * 新增入金支付-集成交易对象信息
     *
     * @param account
     * @return
     */
    public int insert(EntryExitAccount account) {
        account.setClientStatus("0");
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
     * 查询订单不同状态的数据信息
     *
     * @param status
     */
    public List<EntryExitAccount> findByStatus(String status) {
        return mapper.findByStatus(status);
    }

    /**
     * 根据id修改入金支付对象的信息
     *
     * @param id
     * @param account
     * @return
     */
    public int update(int id, EntryExitAccount account) {
        return mapper.update(id, account);
    }

    /**
     * 支付直通车,异步交易通知地址信息
     *
     * @return
     */
    public NoticeResponse orderScantoPay(HttpServletRequest req, HttpServletResponse resp) {
        NoticeRequest noticeRequest = null;
        NoticeResponse noticeResponse = null;
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            // 1 获得参数ptncode, trdcode, message和signature
            String ptncode = req.getParameter("ptncode");
            String trdcode = req.getParameter("trdcode");
            String message = req.getParameter("message");
            String signature = req.getParameter("signature");
            logger.info(TAG_ + "ptncode=" + ptncode);
            logger.info(TAG_ + "trdcode=" + trdcode);
            logger.info(TAG_ + "message=" + message);
            logger.info(TAG_ + "signature=" + signature);
            if (StringUtil.isEmpty(ptncode) || StringUtil.isEmpty(trdcode) || StringUtil.isEmpty(message) || StringUtil.isEmpty(signature)) {
                Map<String, String> map = new TreeMap<>();
                NoticeResponse response = new NoticeResponse();
                map.put("ptncode", ptncode);
                map.put("trdcode", trdcode);
                map.put("message", message);
                map.put("signature", signature);
                response.setMsghd_rspcode("SDER04");
                response.setMsghd_rspmsg("参数错误");
                return response;
            }
            // 2 生成交易请求对象(验签)
            try {
                noticeRequest = new NoticeRequest(message, signature);
            } catch (NullPointerException e) {
                NoticeResponse response = new NoticeResponse();
                response.setMsghd_rspcode("YQ0001");
                response.setMsghd_rspmsg("验签失败");
                response.setSrl_ptnsrl(null);
                Map<String,String> map = new HashMap<>();
                map.put("code",response.getMsghd_rspcode());
                map.put("msg",response.getMsghd_rspmsg());
                logger.info(TAG_ + "通知报文: " + map);
                return response;
            }
            logger.info(TAG_ + "通知报文: " + noticeRequest.getPlainText());
            Map<Object, Object> toXmlMap = com.example.asc.asc.util.StringUtil.jsonToMap(XmlUtil.xmlStrToMap(noticeRequest.getPlainText()).get("MSG"));
            String SrcPtnSrl = com.example.asc.asc.util.StringUtil.jsonToMap(toXmlMap.get("Srl")).get("SrcPtnSrl").toString();
            //获取到给下游客户返回的数据信息
            TreeMap<String, Object> map;
            // 3 业务处理  接收到上游的支付返回成功的信息通知
            //根据交易流水号修改该条交易的状态
            EntryExitAccount account = findByPtnSrl(SrcPtnSrl);
            if (noticeRequest.getMsghd_trcd().equals("T2008")) {
                account.setStatus("1");
                update(account.getId(), account);
                //给上游客户响应信息
                noticeResponse = getNoticeResponse("000000", "业务办理成功", SrcPtnSrl);
                /**
                 * 获取到下游通知地址信息向下游客户发送消息并通知下游客户支付成功
                 */
                map = getDownstream(toXmlMap, "支付成功", SrcPtnSrl, "000000");
                logger.info(TAG_ + "返回给下游的信息" + map);
                //根据交易流水号获取到入金支付交易信息
                if (!StringUtil.isEmpty(account.getServnoticeUrl())) {
                    logger.info(TAG_ + "返回给下游的地址信息" + account.getServnoticeUrl());
                    int num = 0;
                    doPostOrGet(account.getServnoticeUrl(), map, num, account.getSendToClientTimes(), account);
                }
            } else {
                account.setStatus("2");
                update(account.getId(), account);
                //给上游客户响应信息
                noticeResponse = getNoticeResponse("ERROR", "业务办理失败", SrcPtnSrl);
                /**
                 * 获取到下游通知地址信息向下游客户发送消息并通知下游客户支付失败
                 */
                map = getDownstream(toXmlMap, "支付失败", SrcPtnSrl, "0000001");
                //根据交易流水号获取到入金支付交易信息
                if (!StringUtil.isEmpty(account.getServnoticeUrl())) {
                    logger.info(TAG_ + "返回给下游的地址信息" + account.getServnoticeUrl());
                    logger.info(TAG_ + "返回给下游的信息" + map);
                    int num = 0;
                    doPostOrGet(account.getServnoticeUrl(), map, num, account.getSendToClientTimes(), account);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noticeResponse;
    }

    /**
     * 内部异步消息通知地址
     *
     * @return
     */
    public String myOrderScantoPay(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            //获取到data数据
            String data = req.getParameter("data");
            logger.info("内部异步消息通知地址:" + data);
            if (req.getParameter("code").equals("000000")) {
                logger.info("内部异步消息通知地址:000000");
                Map<Object, Object> map = com.example.asc.asc.util.StringUtil.jsonToMap(data);
                String SrcPtnSrl = map.get("SrcPtnSrl").toString();
                //根据客户流水单号信息查询到对应的订单信息,并将其修改为接收到消息
                EntryExitAccount account = findByPtnSrl(SrcPtnSrl);
                account.setClientStatus("1");
                update(account.getId(), account);
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回给下游客户的数据信息
     *
     * @param toXmlMap
     * @param msg
     * @param SrcPtnSrl
     * @return
     */
    public TreeMap<String, Object>  getDownstream(Map<Object, Object> toXmlMap, String msg, String SrcPtnSrl, String code) {
        TreeMap<String, Object>  hashMap = new TreeMap<>();
        TreeMap<String, Object>  map = new TreeMap<>();
        hashMap.put("code", code);
        hashMap.put("msg", msg);
        map.put("SrcPtnSrl", SrcPtnSrl);
        map.put("AclAmt", com.example.asc.asc.util.StringUtil.jsonToMap(toXmlMap.get("Amt")).get("AclAmt").toString());
        hashMap.put("data", map);
        //对数据进行签名验证
        EntryExitAccount entryExitAccount = findByPtnSrl(SrcPtnSrl);
        Users users = usersService.findById(entryExitAccount.getUserId());
        String string = checkDigest(users.getAppId(),hashMap);
        hashMap.put("digest",string);
        return hashMap;
    }

    /**
     * 返回上游客户的信息
     *
     * @param code
     * @param msg
     * @param srl_ptnsrl
     * @return
     */
    private NoticeResponse getNoticeResponse(String code, String msg, String srl_ptnsrl) {
        NoticeResponse noticeResponse = new NoticeResponse();
        noticeResponse.setMsghd_rspcode(code);
        noticeResponse.setMsghd_rspmsg(msg);
        noticeResponse.setSrl_ptnsrl(srl_ptnsrl);
        return noticeResponse;
    }


    /**
     * 对数据进行签名的数据校验
     *
     * @param request  request请求
     * @param response
     * @param appid
     * @return
     */
    public String checkDigest(HttpServletRequest request, HttpServletResponse response, TreeMap<String, String> treeMap, String appid) {
        String digest = null;
        String timestamp = request.getParameter("timestamp");
        try {
            String sortvalue = usersService.findAppId(appid).getSecret();
            for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                sortvalue += entry.getValue().trim();
            }
            digest = Base64.getBase64(
                    SecuritySHA1Utils.shaEncode(
                            appid +
                                    MD5.md5(timestamp).toUpperCase() +
                                    SecuritySHA1Utils.shaEncode(sortvalue).toUpperCase())
                            .toUpperCase()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }


    public String checkDigest(String appid, String timestamp, TreeMap<String, String> treeMap) {
        String digest = null;
        try {
            String sortvalue = usersService.findAppId(appid).getSecret();
            for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                sortvalue += entry.getValue().trim();
            }
            digest = Base64.getBase64(
                    SecuritySHA1Utils.shaEncode(
                            appid +
                                    MD5.md5(timestamp).toUpperCase() +
                                    SecuritySHA1Utils.shaEncode(sortvalue).toUpperCase())
                            .toUpperCase()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;

    }

    /**
     * 获取request的参数信息
     *
     * @param request
     * @return
     */
    public TreeMap<String, String> getDigest(HttpServletRequest request) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        Enumeration<String> enu = request.getParameterNames();
        String t;
        while (enu.hasMoreElements()) {
            t = enu.nextElement();
            if (!t.equals("appid")) {
                if (!t.equals("timestamp")) {
                    if (!t.equals("digest")) {
                        treeMap.put(t, request.getParameter(t));
                    }
                }
            }
        }
        return treeMap;
    }

}
