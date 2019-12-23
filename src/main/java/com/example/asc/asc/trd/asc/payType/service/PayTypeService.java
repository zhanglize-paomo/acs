package com.example.asc.asc.trd.asc.payType.service;

/**
 * 微信菜单业务类
 * @author lujunjie
 * @date   2018/03/01
 */
public interface PayTypeService {


    /**
     * 生成支付二维码URL
     * @param totalFee 标价金额(分单位)
     * @param outTradeNo 商户订单号
     * @param signType 签名类型
     * @throws Exception
     */
    String wxPayUrl(Double totalFee, String outTradeNo, String signType) throws Exception;

}
