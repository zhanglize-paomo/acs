package com.example.asc.asc.trd.asc.applicationfordeposit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 出金-申请[T2022]
 *
 * @author zhanglize
 * @create 2019/11/6
 */
@Controller
@RequestMapping("/application-deposit")
@CrossOrigin
public class ApplicationDepositController {

    private ApplicationDepositService service;
    @Autowired
    public void setService(ApplicationDepositService service) {
        this.service = service;
    }

    /**
     * 出金-申请[T2022]
     *
     * @return
     */
    @RequestMapping(value = "apply",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> applicationDeposit(HttpServletRequest request, HttpServletResponse response) {
        return service.applicationDeposit(request, response);
    }

    /**
     * 出入金结果查询[T2012]
     *
     * @return
     */
    @RequestMapping(value = "query",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,String> queryApplicationDeposit(HttpServletRequest request, HttpServletResponse response) {
        return service.queryApplicationDeposit(request, response);
    }
}
