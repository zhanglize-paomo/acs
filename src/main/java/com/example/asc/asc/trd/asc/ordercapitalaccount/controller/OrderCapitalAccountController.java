package com.example.asc.asc.trd.asc.ordercapitalaccount.controller;

import com.example.asc.asc.trd.asc.ordercapitalaccount.domain.OrderCapitalAccount;
import com.example.asc.asc.trd.asc.ordercapitalaccount.service.OrderCapitalAccountService;
import com.example.asc.asc.trd.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单支付controller
 *
 * @author zhanglize
 * @create 2019/11/19
 */
@Controller
@RequestMapping("/order-capital-account")
@CrossOrigin
public class OrderCapitalAccountController {

    private OrderCapitalAccountService service;

    @Autowired
    public void setService(OrderCapitalAccountService service) {
        this.service = service;
    }

    /**
     * 订单支付
     *
     * @return
     */
    @RequestMapping(value = "orderpay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse orderCapitalAccount(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = checkData(request, response);
        //对数据进行校验
        if (baseResponse != null) {
            if (baseResponse.getCode() != null) {
                return baseResponse;
            }
        }
        return service.orderCapitalAccount(request, response);
    }

    /**
     * 校验数据信息
     *
     * @param request
     * @param response
     * @return
     */
    private BaseResponse checkData(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = new BaseResponse();

        return null;
    }
}