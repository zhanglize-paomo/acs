package com.example.asc.asc.trd.asc.entryexitaccount.controller;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.asc.users.domain.Users;
import com.example.asc.asc.trd.asc.users.service.UsersService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.util.GenerateOrderNoUtil;
import com.example.asc.asc.util.StringUtil;
import com.trz.netwk.api.ntc.NoticeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 入金直通车_异步交易[T2031]控制器
 *
 * @author zhanglize
 * @create 2019/11/8
 */
@Controller
@RequestMapping("/entry-exit-account")
@CrossOrigin
public class EntryExitAccountController {

    private EntryExitAccountService service;
    private UserAccountService userAccountService;
    private UsersService usersService;

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Autowired
    public void setService(EntryExitAccountService service) {
        this.service = service;
    }

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 根据参数生成签名信息的接口
     *
     * @return
     */
    @RequestMapping(value = "check-digest", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse checkDigest(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = new BaseResponse();
        String digest = service.checkDigest(request, response);
        if (!StringUtils.isEmpty(digest)) {
            baseResponse.setCode("200");
            baseResponse.setMsg("生成签名成功");
            baseResponse.setData(digest);
        }
        return baseResponse;
    }

    /**
     * 支付直通车,异步交易通知地址信息
     *
     * @return
     */
    @RequestMapping(value = "notifyurl", method = RequestMethod.POST)
    @ResponseBody
    public NoticeResponse orderScantoPay(HttpServletRequest request, HttpServletResponse response) {
        //对数据进行校验
        return service.orderScantoPay(request, response);
    }

    /**
     * 支付,调用云闪付的平台进行支付
     *
     * @return
     */
    @RequestMapping(value = "scantopay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse scantoPay(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = checkData(request, response);
        //对数据进行校验
        if (baseResponse != null) {
            if (baseResponse.getCode() != null) {
                return baseResponse;
            }
        }
        return service.scantoPay(request, response);
    }

    /**
     * 内部使用支付接口信息
     *
     * @return
     */
    @RequestMapping(value = "my-scantopay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse myScantoPay(HttpServletRequest request, HttpServletResponse response) {
        UserAccount userAccount = userAccountService.findBySubNo(request.getParameter("subNo"));
        Users users = usersService.findById(userAccount.getUserId());
        String appid = users.getAppId();
        String timestamp = request.getParameter("timestamp");
        if (StringUtils.isEmpty(timestamp)) {
            timestamp = String.valueOf(System.currentTimeMillis());
        }
        String ptnSrl = request.getParameter("ptnSrl");
        if (StringUtils.isEmpty(ptnSrl)) {
            ptnSrl = GenerateOrderNoUtil.gen("zf", 530L);
        }
        String goodsDesc = "内部商品";
        String subject = "内部商品";
        String servNoticeUrl = "http://39.107.40.13:8080/entry-exit-account/notifyurl";
        request.setAttribute("appid", appid);
        request.setAttribute("timestamp", timestamp);
        request.setAttribute("ptnSrl", ptnSrl);
        request.setAttribute("goodsDesc", goodsDesc);
        request.setAttribute("subject", subject);
        request.setAttribute("servNoticeUrl", servNoticeUrl);
        //生产签名的信息
        String digest = service.checkDigest(request, response);
        request.setAttribute("digest", digest);
        BaseResponse baseResponse = checkData(request, response);
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", timestamp);
        //对数据进行校验
        if (baseResponse != null) {
            if (baseResponse.getCode() != null) {
                baseResponse.setData(map);
                return baseResponse;
            }
        }
        BaseResponse baseResponse1 = service.scantoPay(request, response);
        if (baseResponse1.getData() != null) {
            Map<Object, Object> stringObjectMap = StringUtil.jsonToMap(JSONObject.fromObject(baseResponse1.getData()));
            stringObjectMap.put("timestamp", timestamp);
            baseResponse1.setData(JSONObject.fromObject(stringObjectMap));
        } else {
            baseResponse1.setData(map);
        }
        return service.scantoPay(request, response);
    }

    /**
     * 对支付功能接口的参数的非空校验
     *
     * @param request
     * @param response
     * @return
     */
    private BaseResponse checkData(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = new BaseResponse();
        String appid = request.getParameter("appid");  //客户的唯一标示
        if (StringUtils.isEmpty(appid)) {
            baseResponse.setCode("ZF300");
            baseResponse.setMsg("请检查客户的唯一标示");
            baseResponse.setData(null);
            return baseResponse;
        }
        String money = request.getParameter("money");  //金额(以分为单位)
        if (StringUtils.isEmpty(money)) {
            baseResponse.setCode("ZF301");
            baseResponse.setMsg("请检查支付金额");
            baseResponse.setData(null);
            return baseResponse;
        }
        String ptnSrl = request.getParameter("ptnSrl");  //客户方流水号（客户程序逻辑生成）
        if (StringUtils.isEmpty(ptnSrl)) {
            baseResponse.setCode("ZF302");
            baseResponse.setMsg("客户方流水号不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        //根据客户方交易流水号判断该交易流水号是否存在
        EntryExitAccount account = service.findByPtnSrl(ptnSrl);
        if (account != null) {
            baseResponse.setCode("ZF303");
            baseResponse.setMsg("客户方流水号已经存在");
            baseResponse.setData(null);
            return baseResponse;
        }
        String subNo = request.getParameter("subNo");  //客户资金账号
        if (StringUtils.isEmpty(subNo)) {
            baseResponse.setCode("ZF304");
            baseResponse.setMsg("客户资金账户不能为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        UserAccount userAccount = userAccountService.findBySubNo(subNo);
        //判断客户资金账户是否存在
        if (userAccount == null) {
            baseResponse.setCode("ZF305");
            baseResponse.setMsg("客户资金账户不存在");
            baseResponse.setData(null);
            return baseResponse;
        }
        //客户发送请求信息的后台异步消息通知地址信息
        String servNoticeUrl = request.getParameter("servNoticeUrl");  //后台异步通知url
        if (StringUtils.isEmpty(servNoticeUrl)) {
            baseResponse.setCode("ZF306");
            baseResponse.setMsg("后台异步通知url不能为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String subject = request.getParameter("subject");  //商品主题描述
        if (StringUtils.isEmpty(subject)) {
            baseResponse.setCode("ZF307");
            baseResponse.setMsg("商品主题描述不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String goodsDesc = request.getParameter("goodsDesc");  //商品描述
        if (StringUtils.isEmpty(goodsDesc)) {
            baseResponse.setCode("ZF308");
            baseResponse.setMsg("商品描述不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String timestamp = request.getParameter("timestamp");  //unix时间戳
        if (StringUtils.isEmpty(timestamp)) {
            baseResponse.setCode("ZF309");
            baseResponse.setMsg("unix时间戳不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        String digest = request.getParameter("digest");  //签名
        if (StringUtils.isEmpty(digest)) {
            baseResponse.setCode("ZF310");
            baseResponse.setMsg("签名信息不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        //对签名的信息进行数据的校验
        String redigest = service.checkDigest(request, response);
        if (!StringUtils.isEmpty(redigest)) {
            if (!redigest.equals(digest)) {
                baseResponse.setCode("ZF311");
                baseResponse.setMsg("签名信息验证失败");
                baseResponse.setData(null);
                return baseResponse;
            }
        }
        return null;
    }
}
