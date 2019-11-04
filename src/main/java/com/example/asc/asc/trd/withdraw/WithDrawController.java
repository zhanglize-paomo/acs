package com.example.asc.asc.trd.withdraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
        Map<String, String> result = new HashMap<String, String>();
        result.put("code", "0");//成功
        try {
            Map<String, String> map = service.withDraw(request, response);

//            result.put("rtnCode",map.get("rtnCode"));
//            result.put("rtnMsg",map.get("rtnMsg"));
//            if(!map.get("rtnCode").equals("0000")){
//                result.put("msg", "代付失败");
//            }else{
//                result.put("msg", "success");
//            }
//            result.put("rtnCode",map.get("rtnCode"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
