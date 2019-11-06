package com.example.asc.asc.trd.asc.applydepositaccount.controller;

import com.example.asc.asc.trd.asc.applydepositaccount.service.ApplyDepositAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 出金申请记录信息控制器
 *
 * @author zhanglize
 * @create 2019/11/6
 */
@Controller
@RequestMapping("/apply-deposit-account")
public class ApplyDepositAccountController {

    private ApplyDepositAccountService service;

    @Autowired
    public void setService(ApplyDepositAccountService service) {
        this.service = service;
    }


}
