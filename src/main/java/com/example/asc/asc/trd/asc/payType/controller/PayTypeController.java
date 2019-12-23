package com.example.asc.asc.trd.asc.payType.controller;


import com.example.asc.asc.trd.asc.payType.common.wx.WxUtil;
import com.example.asc.asc.trd.asc.payType.service.impl.PayTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成支付二维码类
 *
 * @author lujunjie
 * @date 2018/03/01
 */
@Controller
public class PayTypeController {

    @Autowired
    private PayTypeServiceImpl payTypeService;


    /**
     * 二维码首页
     */
    @RequestMapping(value = {"/homePage"}, method = RequestMethod.GET)
    public String wxPayList(Model model) {
        return "/payList";
    }

    /**
     * 生成支付二维码URL
     *
     * @param totalFee 标价金额(分单位)
     * @param payType  支付类型
     * @throws Exception
     */
    @RequestMapping(value = {"/pay/payUrl"})
    public void payUrl(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "totalFee") Double totalFee,
                       @RequestParam(value = "payType") String payType) throws Exception {
        WxUtil.writerPayImage(response, payTypeService.payUrl(totalFee, payType));
    }

}
