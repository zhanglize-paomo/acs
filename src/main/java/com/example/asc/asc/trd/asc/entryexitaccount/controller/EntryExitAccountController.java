package com.example.asc.asc.trd.asc.entryexitaccount.controller;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * 入金直通车_异步交易[T2031]控制器
 *
 * @author zhanglize
 * @create 2019/11/8
 */
@Controller
@RequestMapping("/entry-exit-account")
public class EntryExitAccountController {

    private EntryExitAccountService service;
    @Autowired
    public void setService(EntryExitAccountService service) {
        this.service = service;
    }

    private UserAccountService userAccountService;
    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 支付直通车,异步交易通知地址信息
     *
     * @return
     */
    @RequestMapping(value = "orderscantopay",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>  orderScantoPay(HttpServletRequest request, HttpServletResponse response) {
        //对数据进行校验
        checkData(request);
        return service.orderScantoPay(request, response);
    }


    /**
     * H5支付,调用云闪付的平台进行支付
     *
     * @return
     */
    @RequestMapping(value = "scantopay",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>  scantoPay(HttpServletRequest request, HttpServletResponse response) {
        //对数据进行校验
        checkData(request);
        return service.scantoPay(request, response);
    }


    /**
     * 对支付功能接口的参数的非空校验
     *
     *
     * @param request
     * @return
     */
    private Map<String,String> checkData(HttpServletRequest request) {
        Map<String, String> treeMap = new TreeMap<>();
        Map<String, String> map = new TreeMap<>();
        String appid = request.getParameter("appid");  //客户的唯一标示
        if(StringUtils.isEmpty(appid)){
            treeMap.put("code","ZF300");
            treeMap.put("url",null);
            treeMap.put("msg","请检查客户的唯一标示");
            return treeMap;
        }
        String money = request.getParameter("money");  //金额(以分为单位)
        if(StringUtils.isEmpty(money)){
            treeMap.put("code","ZF301");
            treeMap.put("url",null);
            treeMap.put("msg","请检查支付金额");
            return treeMap;
        }
        String ptnSrl = request.getParameter("ptnSrl");  //客户方流水号（客户程序逻辑生成）
        if(StringUtils.isEmpty(ptnSrl)){
            treeMap.put("code","ZF302");
            treeMap.put("url",null);
            treeMap.put("msg","客户方流水号不可为空");
            return treeMap;
        }
        //根据客户方交易流水号判断该交易流水号是否存在
        EntryExitAccount account = service.findByPtnSrl(ptnSrl);
        if(account != null){
            treeMap.put("code","ZF303");
            treeMap.put("url",null);
            treeMap.put("msg","客户方流水号已经存在");
            return treeMap;
        }
        String subNo = request.getParameter("subNo");  //客户资金账号
        if(StringUtils.isEmpty(subNo)){
            treeMap.put("code","ZF304");
            treeMap.put("url",null);
            treeMap.put("msg","客户资金账户不能为空");
            return treeMap;
        }
        UserAccount userAccount = userAccountService.findBySubNo(subNo);
        //判断客户资金账户是否存在
        if(userAccount == null){
            treeMap.put("code","ZF305");
            treeMap.put("url",null);
            treeMap.put("msg","客户资金账户不存在");
            return treeMap;
        }
        //客户发送请求信息的后台异步消息通知地址信息
        String servNoticeUrl = request.getParameter("servNoticeUrl");  //后台异步通知url
        if(StringUtils.isEmpty(servNoticeUrl)){
            treeMap.put("code","ZF306");
            treeMap.put("url",null);
            treeMap.put("msg","后台异步通知url不能为空");
            return treeMap;
        }
        String subject = request.getParameter("subject");  //商品主题描述
        if(StringUtils.isEmpty(subject)){
            treeMap.put("code","ZF307");
            treeMap.put("url",null);
            treeMap.put("msg","商品主题描述不可为空");
            return treeMap;
        }
        String goodsDesc = request.getParameter("goodsDesc");  //商品描述
        if(StringUtils.isEmpty(goodsDesc)){
            treeMap.put("code","ZF308");
            treeMap.put("url",null);
            treeMap.put("msg","商品描述不可为空");
            return treeMap;
        }
        String timestamp = request.getParameter("timestamp");  //unix时间戳
        if(StringUtils.isEmpty(timestamp)){
            treeMap.put("code","ZF309");
            treeMap.put("url",null);
            treeMap.put("msg","unix时间戳不可为空");
            return treeMap;
        }
        String digest = request.getParameter("digest");  //签名
        if(StringUtils.isEmpty(digest)){
            treeMap.put("code","ZF300");
            treeMap.put("url",null);
            treeMap.put("msg","签名信息不可为空");
            return treeMap;
        }
        map.put("appid",appid);
        map.put("money",money);
        map.put("ptnSrl",ptnSrl);
        map.put("subNo",subNo);
        map.put("servNoticeUrl",servNoticeUrl);
        map.put("subject",subject);
        map.put("goodsDesc",goodsDesc);
        map.put("timestamp",timestamp);
        //对签名的信息进行数据的校验
        checkDigest(map,digest);
        return null;
    }

    /**
     * 对数据进行签名的数据校验
     *
     * @param map
     * @param digest
     * @return
     */
    private Map<String, String> checkDigest(Map<String, String> map, String digest) {


        return null;
    }

}
