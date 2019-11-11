package com.example.asc.asc.trd.asc.useraccount.controller;

import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户申请信息控制器
 *
 * @author zhanglize
 * @create 2019/11/6
 */
@Controller
@RequestMapping("/user-account")
@CrossOrigin
public class UserAccountController {

    private UserAccountService service;
    @Autowired
    public void setService(UserAccountService service) {
        this.service = service;
    }
}
