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
     * @param payType 支付类型
     * @throws Exception
     */
    String payUrl(Double totalFee, String payType) throws Exception;

}
