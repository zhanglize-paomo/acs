package com.example.asc.asc.trd.asc.entryexitaccount.controller;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.util.*;
import com.trz.netwk.api.ntc.NoticeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 入金直通车_异步交易[T2031]控制器
 *
 * @author zhanglize
 * @create 2019/11/8
 */
@Controller
@RequestMapping("/entry-exit-account")
@CrossOrigin
public class EntryExitAccountController {

    private EntryExitAccountService service;
    private UserAccountService userAccountService;

    /**
     * 对数据进行签名的数据校验
     *
     * @param map
     * @param digest
     * @return
     */
    private static String checkDigest(Map<String, String> map, String digest) {
        try {
            String appid = "=Wq4Nc1oA5EW8ZlSaYYl8NmSGrtTNC";
            String timestamp = "1564652780";
            String secret = "NSN8KroSxdHJxfJ8bYsHOlWvPBpj30";
            Map<String, Object> stringObjectMap = StringUtil.StringToMap(SortUtils.Ksort(map));
            String sortvalue = secret;
            for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                sortvalue += entry.getValue();
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

    public static void main(String[] args) throws Exception {
//        String digest = "NjY0MDdGNjJEMjgzMkNBQURDNjAwMTNEMDVEODY0NDBGMjcxN0M2OQ==";
//        Map<String, String> map = new TreeMap<>();
//        map.put("fcFlg", "1");
//        map.put("ptnSrl", "20191107153022");
//        map.put("subNo", "1931115000186036");
//        map.put("bkId", "105");
//        map.put("accNo", "6217000260012247023");
//        map.put("accNm", "张李泽");
//        map.put("accTp", "2");
//        map.put("crdTp", "1");
//        map.put("cdTp", "A");
//        map.put("cdNo", "142729199604031815");
//        map.put("crsMk", "1");
//        map.put("phone", "18434395962");
//        String str = checkDigest(map, null);
//        System.out.println(str);
//        System.out.println(digest);
//        if (str.equals(digest)) {
//            System.out.println("31313123");
//        }

    }



    @Autowired
    public void setService(EntryExitAccountService service) {
        this.service = service;
    }

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 支付直通车,异步交易通知地址信息
     *
     * @return
     */
    @RequestMapping(value = "notifyurl", method = RequestMethod.POST)
    @ResponseBody
    public NoticeResponse orderScantoPay(HttpServletRequest request, HttpServletResponse response) {
        //对数据进行校验
        return service.orderScantoPay(request, response);
    }

    /**
     * 支付,调用云闪付的平台进行支付
     *
     * @return
     */
    @RequestMapping(value = "scantopay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse scantoPay(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = checkData(request);
        //对数据进行校验
        if (baseResponse != null) {
            if (baseResponse.getCode() != null) {
                return baseResponse;
            }
        }
        return service.scantoPay(request, response);
    }

    /**
     * 对支付功能接口的参数的非空校验
     *
     * @param request
     * @return
     */
    private BaseResponse checkData(HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        String appid = request.getParameter("appid");  //客户的唯一标示
        if (StringUtils.isEmpty(appid)) {
            baseResponse.setCode("ZF300");
            baseResponse.setMsg("请检查客户的唯一标示");
            baseResponse.setData(null);
            return baseResponse;
        }
        String money = request.getParameter("money");  //金额(以分为单位)
        if (StringUtils.isEmpty(money)) {
            baseResponse.setCode("ZF301");
            baseResponse.setMsg("请检查支付金额");
            baseResponse.setData(null);
            return baseResponse;
        }
        String ptnSrl = request.getParameter("ptnSrl");  //客户方流水号（客户程序逻辑生成）
        if (StringUtils.isEmpty(ptnSrl)) {
            baseResponse.setCode("ZF302");
            baseResponse.setMsg("客户方流水号不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        //根据客户方交易流水号判断该交易流水号是否存在
        EntryExitAccount account = service.findByPtnSrl(ptnSrl);
        if (account != null) {
            baseResponse.setCode("ZF303");
            baseResponse.setMsg("客户方流水号已经存在");
            baseResponse.setData(null);
            return baseResponse;
        }
        String subNo = request.getParameter("subNo");  //客户资金账号
        if (StringUtils.isEmpty(subNo)) {
            baseResponse.setCode("ZF304");
            baseResponse.setMsg("客户资金账户不能为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        UserAccount userAccount = userAccountService.findBySubNo(subNo);
        //判断客户资金账户是否存在
        if (userAccount == null) {
            baseResponse.setCode("ZF305");
            baseResponse.setMsg("客户资金账户不存在");
            baseResponse.setData(null);
            return baseResponse;
        }
        //客户发送请求信息的后台异步消息通知地址信息
        String servNoticeUrl = request.getParameter("servNoticeUrl");  //后台异步通知url
        if (StringUtils.isEmpty(servNoticeUrl)) {
            baseResponse.setCode("ZF306");
            baseResponse.setMsg("后台异步通知url不能为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String subject = request.getParameter("subject");  //商品主题描述
        if (StringUtils.isEmpty(subject)) {
            baseResponse.setCode("ZF307");
            baseResponse.setMsg("商品主题描述不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String goodsDesc = request.getParameter("goodsDesc");  //商品描述
        if (StringUtils.isEmpty(goodsDesc)) {
            baseResponse.setCode("ZF308");
            baseResponse.setMsg("商品描述不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String timestamp = request.getParameter("timestamp");  //unix时间戳
        if (StringUtils.isEmpty(timestamp)) {
            baseResponse.setCode("ZF309");
            baseResponse.setMsg("unix时间戳不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
//        String digest = request.getParameter("digest");  //签名
//        if (StringUtils.isEmpty(digest)) {
//            treeMap.put("code", "ZF300");
//            treeMap.put("url", null);
//            treeMap.put("msg", "签名信息不可为空");
//            return treeMap;
//        }
//        map.put("appid", appid);
//        map.put("money", money);
//        map.put("ptnSrl", ptnSrl);
//        map.put("subNo", subNo);
//        map.put("servNoticeUrl", servNoticeUrl);
//        map.put("subject", subject);
//        map.put("goodsDesc", goodsDesc);
//        map.put("timestamp", timestamp);
        //对签名的信息进行数据的校验
//        checkDigest(map, digest);
        return null;
    }


}
