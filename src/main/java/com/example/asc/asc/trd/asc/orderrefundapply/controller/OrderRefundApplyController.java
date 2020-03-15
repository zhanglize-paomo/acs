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
        if(service.findByPtnSrl(request.getParameter("srl_ptnsrl")) != null){
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setMsg("交易流水号已经存在");
            baseResponse.setCode("TK000004");
            baseResponse.setData(null);
            return baseResponse;
        }
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


    /**
     * 退款申请导出Excel表格
     *
     */
    @ResponseBody
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportPayCustomerDetail(HttpServletRequest request,HttpServletResponse response) {
        service.exportPayCustomerDetail(request, response);
    }


}
