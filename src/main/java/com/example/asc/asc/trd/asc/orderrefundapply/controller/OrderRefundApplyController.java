package com.example.asc.asc.trd.asc.orderrefundapply.controller;

import com.example.asc.asc.trd.asc.orderrefundapply.service.OrderRefundApplyService;
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
 * 退款申请Controller
 *
 * @author zhanglize
 * @create 2020/1/3
 */
@Controller
@RequestMapping("/order-refund-apply")
@CrossOrigin
public class OrderRefundApplyController {

    private OrderRefundApplyService service;
    @Autowired
    public void setService(OrderRefundApplyService service) {
        this.service = service;
    }

    /**
     * 退款申请[T4041]
     *
     * @return
     */
    @RequestMapping(value = "orderrefund", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse orderRefundApply(HttpServletRequest request, HttpServletResponse response) {
        return service.orderRefundApply(request, response);
    }

    /**
     * 查询退款申请结果[T4043]
     *
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse queryOrderRefundApply(HttpServletRequest request, HttpServletResponse response) {
        return service.queryOrderRefundApply(request, response);
    }


}
