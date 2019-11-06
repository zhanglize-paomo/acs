package com.example.asc.asc.trd.asc.withdraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 查询可 T0/T1 出金额度[T1018]
 *
 * @author zhanglize
 * @create 2019/11/4
 */
@Controller
@RequestMapping("/with-draw")
public class WithDrawController {

    private WithDrawService service;

    @Autowired
    public void setService(WithDrawService service) {
        this.service = service;
    }

    /**
     * 查询可 T0/T1 出金额度
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String,String> withDraw(HttpServletRequest request, HttpServletResponse response) {
        return service.withDraw(request, response);
    }

}
