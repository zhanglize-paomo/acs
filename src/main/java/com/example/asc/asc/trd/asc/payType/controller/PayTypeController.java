package com.example.asc.asc.trd.asc.payType.controller;


import com.example.asc.asc.trd.asc.payType.common.wx.WxConstants;
import com.example.asc.asc.trd.asc.payType.common.wx.WxUtil;
import com.example.asc.asc.trd.asc.payType.service.PayTypeService;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 微信菜单控制类
 * @author lujunjie
 * @date   2018/03/01
 */
@Controller
public class PayTypeController {

    @Autowired
    private PayTypeService payTypeService;

    /**
     * 二维码首页
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String wxPayList(Model model){
        //商户订单号
        model.addAttribute("outTradeNo", WxUtil.mchOrderNo());
        return "/wxPayList";
    }

    /**
     * 获取流水号
     */
    @RequestMapping(value = {"/wxPay/outTradeNo"})
    @ResponseBody
    public String getOutTradeNo(Model model){
        //商户订单号
        return WxUtil.mchOrderNo();
    }

    final private String signType = WxConstants.SING_MD5;
    /**
     * 统一下单-生成二维码
     */
    @RequestMapping(value = {"/wxPay/payUrl"})
    public void payUrl(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "totalFee")Double totalFee,
                       @RequestParam(value = "outTradeNo")String outTradeNo) throws Exception{
        WxUtil.writerPayImage(response, payTypeService.wxPayUrl(totalFee,outTradeNo,signType));
    }

    /**
     * 统一下单-通知链接
     */
    @RequestMapping(value = {"/wxPay/unifiedorderNotify"})
    public void unifiedorderNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{

        //商户订单号
        String outTradeNo = null;
        String xmlContent = "<xml>" +
                "<return_code><![CDATA[FAIL]]></return_code>" +
                "<return_msg><![CDATA[签名失败]]></return_msg>" +
                "</xml>";

        try{
            String requstXml = WxUtil.getStreamString(request.getInputStream());
            System.out.println("requstXml : " + requstXml);
            Map<String,String> map = WxUtil.xmlToMap(requstXml);
            String returnCode= map.get(WxConstants.RETURN_CODE);
            //校验一下
            if(StringUtils.isNotBlank(returnCode) && StringUtils.equals(returnCode,"SUCCESS")
                    //WxUtil.isSignatureValid(map, WxConfig.key,signType)
            ){
                //商户订单号
                outTradeNo = map.get("out_trade_no");
                System.out.println("outTradeNo : "+ outTradeNo);
                //微信支付订单号
                String transactionId = map.get("transaction_id");
                System.out.println("transactionId : "+ transactionId);
                //支付完成时间
                SimpleDateFormat payFormat= new SimpleDateFormat("yyyyMMddHHmmss");
                Date payDate = payFormat.parse(map.get("time_end"));

                SimpleDateFormat systemFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("支付时间：" + systemFormat.format(payDate));
                //临时缓存
                //WxConfig.setPayMap(outTradeNo,"SUCCESS");
                xmlContent = "<xml>" +
                        "<return_code><![CDATA[SUCCESS]]></return_code>" +
                        "<return_msg><![CDATA[OK]]></return_msg>" +
                        "</xml>";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        WxUtil.responsePrint(response,xmlContent);
    }

    /**
     * 定时器查询是否已支付
     */
    @RequestMapping(value = {"/wxPay/payStatus"})
    @ResponseBody
    public String payStatus(@RequestParam(value = "outTradeNo")String outTradeNo){
        JSONObject responseObject = new JSONObject();
        //outTradeNo = WxConfig.getPayMap(outTradeNo);
        String status = "200";
        if(StringUtils.isNotBlank(outTradeNo) && StringUtils.equals(outTradeNo,"SUCCESS")){
            status = "0";
        }
        responseObject.put("status",status);
        return responseObject.toJSONString();
    }





}
