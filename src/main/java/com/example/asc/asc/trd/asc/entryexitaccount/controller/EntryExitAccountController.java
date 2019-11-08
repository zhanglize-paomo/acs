package com.example.asc.asc.trd.asc.entryexitaccount.controller;

import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class EntryExitAccountController {

    private EntryExitAccountService service;
    @Autowired
    public void setService(EntryExitAccountService service) {
        this.service = service;
    }

    /**
     * H5支付,调用云闪付的平台进行支付
     *
     * @return
     */
    @RequestMapping(value = "scantopay",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>  scantoPay(HttpServletRequest request, HttpServletResponse response) {
        return service.scantoPay(request, response);
    }

}
