package com.example.asc.asc.trd.asc.entryexitaccount.controller;

import com.example.asc.asc.trd.asc.cloudflashoverorder.domain.CloudFlashoverOrder;
import com.example.asc.asc.trd.asc.cloudflashoverorder.service.CloudFlashoverOrderService;
import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.asc.users.domain.Users;
import com.example.asc.asc.trd.asc.users.service.UsersService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.util.Base64;
import com.example.asc.asc.util.JsoupHtmlUtils;
import com.example.asc.asc.util.StringUtil;
import com.trz.netwk.api.ntc.NoticeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    private CloudFlashoverOrderService cloudFlashoverOrderService;

    @Autowired
    public void setCloudFlashoverOrderService(CloudFlashoverOrderService cloudFlashoverOrderService) {
        this.cloudFlashoverOrderService = cloudFlashoverOrderService;
    }

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
        TreeMap<String, String> treeMap = service.getDigest(request);
        UserAccount userAccount = userAccountService.findBySubNo(request.getParameter("subNo"));
        String appid = usersService.findById(userAccount.getUserId()).getAppId();
        String timestamp = request.getParameter("timestamp");
        String digest = service.checkDigest(appid, timestamp, treeMap);
        if (!StringUtils.isEmpty(digest)) {
            baseResponse.setCode("200");
            baseResponse.setMsg("生成签名成功");
            Map<String, String> map = new HashMap<>();
            map.put("digest", digest);
            map.put("timestamp", timestamp);
            baseResponse.setData(JSONObject.fromObject(map));
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
     * 跳转页面信息
     *
     * @param id 云闪付支付信息id
     * @return
     */
    @RequestMapping(value = "unifiedOrder/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String unifiedOrder(@PathVariable Long id) {
        CloudFlashoverOrder order = cloudFlashoverOrderService.findById(id);
        //解析code的内容信息
        TreeMap<String, Object> treeMap = JsoupHtmlUtils.getJsoupHtmlUtils(Base64.getFromBase64(order.getAuthCode()));
        String url = "https://gateway.95516.com/gateway/api/frontTransReq.do";
        return JsoupHtmlUtils.createAutoFormHtml(url, treeMap, "UTF-8");
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
     * 银联云闪付 接口信息
     *
     * @return
     */
    @RequestMapping(value = "unionpay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse unionPay(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = checkData(request, response);
        //对数据进行校验
        if (baseResponse != null) {
            if (baseResponse.getCode() != null) {
                return baseResponse;
            }
        }
        return service.unionPay(request, response);
    }

    /**
     * 银联云闪付 接口信息
     *
     * @return
     */
    @RequestMapping(value = "unionpay-html", method = RequestMethod.GET)
    public String unionpay(HttpServletRequest request, HttpServletResponse response) {
        //弹出窗口信息将金额添加到窗口栏中
        request.setAttribute("money", request.getParameter("money"));
        return "unionpay";
    }



    /**
     * 海利盈商场支付接口信息
     *
     * @return
     */
    @RequestMapping(value = "shop-scantopay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse shopScantoPay(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = new BaseResponse();
        String money = request.getParameter("money");  //金额(以分为单位)
        if (StringUtils.isEmpty(money)) {
            baseResponse.setCode("ZF301");
            baseResponse.setMsg("请检查支付金额");
            baseResponse.setData(null);
            return baseResponse;
        }
        String payType = request.getParameter("payType");  //金额(以分为单位)
        if (StringUtils.isEmpty(payType)) {
            baseResponse.setCode("ZF303");
            baseResponse.setMsg("支付方式不可为空");
            baseResponse.setData(null);
            return baseResponse;
        }
        return service.shopScantoPay(request, response);
    }

    /**
     * 内部异步消息通知地址
     *
     * @return
     */
    @RequestMapping(value = "my-notifyurl", method = RequestMethod.POST)
    @ResponseBody
    public String myOrderScantoPay(HttpServletRequest request, HttpServletResponse response) {
        //对数据进行校验
        return service.myOrderScantoPay(request, response);
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
        String servNoticeUrl = "http://39.107.40.13:8080/entry-exit-account/notifyurl";
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("subNo", request.getParameter("subNo"));
        treeMap.put("ptnSrl", request.getParameter("ptnSrl"));
        //根据客户方交易流水号判断该交易流水号是否存在
        EntryExitAccount account = service.findByPtnSrl(request.getParameter("ptnSrl"));
        if (account != null) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setCode("ZF303");
            baseResponse.setMsg("客户方流水号已经存在");
            baseResponse.setData(null);
            return baseResponse;
        }
        treeMap.put("servNoticeUrl", servNoticeUrl);
        treeMap.put("money", request.getParameter("money"));
        treeMap.put("payType", request.getParameter("payType"));
        //生产签名的信息
        String digest = service.checkDigest(appid, timestamp, treeMap);
        treeMap.put("digest", digest);
        treeMap.put("appid", appid);
        treeMap.put("timestamp", timestamp);
        //BaseResponse baseResponse = checkData(treeMap);
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", timestamp);
//        //对数据进行校验
//        if (baseResponse != null) {
//            if (baseResponse.getCode() != null) {
//                baseResponse.setData(map);
//                return baseResponse;
//            }
//        }
        request.setAttribute("servNoticeUrl",servNoticeUrl);
        BaseResponse baseResponse1 = service.scantoPay(request, response);
        if (baseResponse1.getData() != null) {
            Map<Object, Object> stringObjectMap = StringUtil.jsonToMap(JSONObject.fromObject(baseResponse1.getData()));
            stringObjectMap.put("timestamp", timestamp);
            stringObjectMap.put("digest", digest);
            stringObjectMap.put("subNo", request.getParameter("subNo"));
            stringObjectMap.put("ptnSrl", request.getParameter("ptnSrl"));
            stringObjectMap.put("servNoticeUrl", servNoticeUrl);
            stringObjectMap.put("money", request.getParameter("money"));
            stringObjectMap.put("payType", request.getParameter("payType"));
            stringObjectMap.put("appid", appid);
            baseResponse1.setData(JSONObject.fromObject(stringObjectMap));
        } else {
            baseResponse1.setData(map);
        }
        return baseResponse1;
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
        if (Long.valueOf(money) > 300000 || Long.valueOf(money) < 5000) {
            baseResponse.setCode("ZF312");
            baseResponse.setMsg("请注意支付金额单笔不能大于300000分不能低于5000分");
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
        //获取request的参数信息
        TreeMap<String, String> treeMap = service.getDigest(request);
        //对签名的信息进行数据的校验
        String redigest = service.checkDigest(request, response, treeMap, request.getParameter("appid"));
        if (!StringUtils.isEmpty(redigest)) {
            if (!redigest.equals(digest)) {
                baseResponse.setCode("ZF311");
                baseResponse.setMsg("签名信息验证失败");
                baseResponse.setData(null);
                return baseResponse;
//                //如果签名不相等,将数据中的appid替换掉
//                //根据subNo查询出对应的appid信息
//                String newAppid = usersService.findById(userAccountService.findBySubNo(subNo).getUserId()).getAppId();
//                String newRedigest = service.checkDigest(request, response, treeMap, newAppid);
            }
        }
        return null;
    }



    /**
     * 导出Excel表格
     *
     */
    @ResponseBody
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportPayCustomerDetail(HttpServletRequest request,HttpServletResponse response) {
        service.exportPayCustomerDetail(request, response);
    }


}
