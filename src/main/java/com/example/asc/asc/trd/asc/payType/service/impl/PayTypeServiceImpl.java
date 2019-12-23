package com.example.asc.asc.trd.asc.payType.service.impl;

import com.example.asc.asc.trd.asc.payType.common.wx.WxUtil;
import com.example.asc.asc.trd.asc.payType.service.PayTypeService;
import org.springframework.stereotype.Service;
import java.util.HashMap;

/**
 * 微信菜单实现类类
 * @author lujunjie
 * @date   2018/02/16
 */
@Service("wxMenuService")
public class PayTypeServiceImpl implements PayTypeService {

    @Override
    public String wxPayUrl(Double totalFee,String outTradeNo,String signType) throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        //公众账号ID
//        data.put("appid", WxConfig.appID);
//        //商户号
//        data.put("mch_id", WxConfig.mchID);
        //随机字符串
        data.put("nonce_str", WxUtil.getNonceStr());
        //商品描述
        data.put("body","测试支付");
        //商户订单号
        data.put("out_trade_no",outTradeNo);
        //标价币种
        data.put("fee_type","CNY");
        //标价金额
        data.put("total_fee",String.valueOf(Math.round(totalFee * 100)));
        //用户的IP
        data.put("spbill_create_ip","123.12.12.123");
        //通知地址
        //data.put("notify_url",WxConfig.unifiedorderNotifyUrl);
        //交易类型
        data.put("trade_type","NATIVE");
        //签名类型
        data.put("sign_type",signType);
        //签名
       // data.put("sign",WxUtil.getSignature(data, WxConfig.key,signType));

        String requestXML = WxUtil.mapToXml(data);
//        String reponseString = HttpsClient.httpsRequestReturnString(WxConstants.PAY_UNIFIEDORDER,HttpsClient.METHOD_POST,requestXML);
//        Map<String,String> resultMap = WxUtil.processResponseXml(reponseString,signType);
//        if(resultMap.get(WxConstants.RETURN_CODE).equals("SUCCESS")){
//            return resultMap.get("code_url");
//        }
        return null;
    }

}
