package com.example.asc.asc.trd.asc.freezingthawingno;

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
 * 冻结/解冻[T3001]
 *
 * @author zhanglize
 * @create 2019/12/30
 */
@Controller
@RequestMapping("/freezing-thawing")
@CrossOrigin
public class FreezingThawingNoController {

    private FreezingThawingNoService service;

    @Autowired
    public void setService(FreezingThawingNoService service) {
        this.service = service;
    }

    /**
     * 解冻[T3001]
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse freezingThawingNo(HttpServletRequest request, HttpServletResponse response) {
        return service.freezingThawingNo(request, response);
    }
}
