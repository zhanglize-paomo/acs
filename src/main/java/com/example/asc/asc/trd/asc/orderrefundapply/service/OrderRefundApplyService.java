package com.example.asc.asc.trd.asc.orderrefundapply.service;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import com.example.asc.asc.trd.asc.entryexitaccount.service.EntryExitAccountService;
import com.example.asc.asc.trd.asc.orderrefundapply.domain.OrderRefundApply;
import com.example.asc.asc.trd.asc.orderrefundapply.mapper.OrderRefundApplyMapper;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.DateUtils;
import com.example.asc.asc.util.SnowflakeIdUtils;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT4041Request;
import com.trz.netwk.api.trd.TrdT4041Response;
import com.trz.netwk.api.trd.TrdT4043Request;
import com.trz.netwk.api.trd.TrdT4043Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退款申请Service
 *
 * @author zhanglize
 * @create 2020/1/3
 */
@Service
public class OrderRefundApplyService {

    private static final Logger logger = LoggerFactory.getLogger(OrderRefundApplyService.class);
    private static final String TAG = "{退款申请}-";
    private OrderRefundApplyMapper mapper;

    private EntryExitAccountService accountService;
    @Autowired
    public void setAccountService(EntryExitAccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setMapper(OrderRefundApplyMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 退款申请
     *
     * @return
     */
    public BaseResponse orderRefundApply(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghd_trdt"));
            /** 合作方交易流水号 */
            String srl_ptnsrl = req.getParameter("srl_ptnsrl");
            /** 业务标示(A00:普通退款) */
            String trsflag = "A00";
            /** 原交易标志(ENTRCV:收款业务;JUHEPAY:聚合支付) */
            String dtrcd = "UIN";
            /** 原交易的合作方交易流水号 */
            String dptnsrl = req.getParameter("dptnsrl");
            /** 退款原因 */
            String usage = "申请退款";
            /** 发生额(资金单位:分) */
            long amt_aclamt = Long.valueOf(req.getParameter("amt_aclamt"));
            OrderRefundApply apply = new OrderRefundApply();
            apply.setMsghdTrdt(msghd_trdt);
            apply.setAmtAclamt(amt_aclamt);
            apply.setSrlPtnsrl(srl_ptnsrl);
            apply.setDptnsrl(dptnsrl);
            insert(apply);
            //加载配置文件信息
            FileConfigure.getFileConfigure("1933216000190594");
            // 2. 实例化交易对象
            TrdT4041Request trdRequest = new TrdT4041Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setSrl_ptnsrl(srl_ptnsrl);
            trdRequest.setTrsflag(trsflag);
            trdRequest.setDtrcd(dtrcd);
            trdRequest.setDptnsrl(dptnsrl);
            trdRequest.setUsage(usage);
            trdRequest.setAmt_aclamt(amt_aclamt);
            // 3. 报文处理
            trdRequest.process();
            logger.info(TAG + "请求报文[" + trdRequest.getRequestPlainText() + "]");
            logger.info(TAG + "签名原文[" + trdRequest.getRequestMessage() + "]");
            logger.info(TAG + "签名数据[" + trdRequest.getRequestSignature() + "]");
            // 4. 与融资平台通信
            TrdMessenger trdMessenger = new TrdMessenger();
            // message
            String respMsg = trdMessenger.send(trdRequest);
            // 5. 处理交易结果
            TrdT4041Response trdResponse = new TrdT4041Response(respMsg);
            logger.info(TAG + "响应报文[" + trdResponse.getResponsePlainText() + "]");
            OrderRefundApply orderRefundApply = findByPtnSrl(trdResponse.getSrl_ptnsrl()); //合作方交易流水号
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                return updateTrdResponse(trdResponse, orderRefundApply);
            } else {
                return updateTrdResponse(trdResponse, orderRefundApply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BaseResponse updateTrdResponse(TrdT4041Response trdResponse, OrderRefundApply orderRefundApply) {
        BaseResponse baseResponse = new BaseResponse();
        logger.info(TAG + "[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]"); // 返回信息
        logger.info(TAG + "[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]"); // 平台交易流水号
        logger.info(TAG + "[state]=[" + trdResponse.getState() + "]");  // 退款结果:1退款成功;2退款失败;3退款处理中
        logger.info(TAG + "[resttime]=[" + trdResponse.getResttime() + "]"); // 退款成功/失败时间(渠道通知时间)格式:YYYYMMDDHH24MISS
        logger.info(TAG + "[opion]=[" + trdResponse.getOpion() + "]");// 失败原因
        orderRefundApply.setSrlPlatsrl(trdResponse.getSrl_platsrl());
        orderRefundApply.setState(trdResponse.getState());
        orderRefundApply.setRestTime(trdResponse.getResttime());
        orderRefundApply.setOpion(trdResponse.getOpion());
        update(orderRefundApply.getId(), orderRefundApply);
        Map<String,String> map = new HashMap<>();
        map.put("resttime", trdResponse.getResttime());
        map.put("Srl_platsrl",trdResponse.getSrl_platsrl());
        if(trdResponse.getState().equals("1")){
            baseResponse.setData(map.toString());
            baseResponse.setCode("TK000000");
            baseResponse.setMsg("退款成功");
            return baseResponse;
        }else if(trdResponse.getState().equals("2")){
            baseResponse.setData(map.toString());
            baseResponse.setCode("TK000001");
            baseResponse.setMsg("退款失败");
            return baseResponse;
        }else{
            baseResponse.setData(map.toString());
            baseResponse.setCode("TK000002");
            baseResponse.setMsg("退款处理中");
            return baseResponse;
        }
    }

    /**
     * 根据id修改对应的退款订单信息
     *
     * @param id
     * @param orderRefundApply
     * @return
     */
    public int update(Long id, OrderRefundApply orderRefundApply) {
        return mapper.update(id, orderRefundApply);
    }

    /**
     * 根据合作方交易流水号查询到对应的退款订单信息
     *
     * @param srl_ptnsrl
     * @return
     */
    public OrderRefundApply findByPtnSrl(String srl_ptnsrl) {
        return mapper.findByPtnSrl(srl_ptnsrl);
    }

    /**
     * 新增退款申请数据信息
     *
     * @param apply
     * @return
     */
    public int insert(OrderRefundApply apply) {
        apply.setId(new SnowflakeIdUtils().nextId());
        apply.setCreatedAt(DateUtils.stringToDate());
        return mapper.insert(apply);
    }

    /**
     * 查询退款申请结果[T4043]
     *
     * @return
     */
    public BaseResponse queryOrderRefundApply(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BaseResponse baseResponse = new BaseResponse();
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            /** 交易日期 */
            String msghd_trdt = req.getParameter("msghd_trdt");
            /** 合作方交易流水号 */
            String srl_ptnsrl = req.getParameter("srl_ptnsrl");
            // 2. 实例化交易对象
            TrdT4043Request trdRequest = new TrdT4043Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setSrl_ptnsrl(srl_ptnsrl);
            //加载配置文件信息
            FileConfigure.getFileConfigure("1933216000190594");
            // 3. 报文处理
            trdRequest.process();
            logger.info(TAG+ "请求报文[" + trdRequest.getRequestPlainText() + "]");
            logger.info(TAG+ "签名原文[" + trdRequest.getRequestMessage() + "]");
            logger.info(TAG+ "签名数据[" + trdRequest.getRequestSignature() + "]");
            // 4. 与融资平台通信
            TrdMessenger trdMessenger = new TrdMessenger();
            // message
            String respMsg = trdMessenger.send(trdRequest);
            // 5. 处理交易结果
            TrdT4043Response trdResponse = new TrdT4043Response(respMsg);
            logger.info(TAG+ "响应报文[" + trdResponse.getResponsePlainText() + "]");
            logger.info(TAG+"[msghd_rspmsg]=[" + trdResponse.getMsghd_rspmsg() + "]"); // 返回信息
            logger.info("[srl_ptnsrl]=[" + trdResponse.getSrl_ptnsrl() + "]"); // T4041交易的合作方交易流水号
            logger.info("[srl_platsrl]=[" + trdResponse.getSrl_platsrl() + "]"); // T4041交易的平台交易流水号
            logger.info("[trsflag]=[" + trdResponse.getTrsflag() + "]"); // 业务标示:A00普通退款
            logger.info("[dtrcd]=[" + trdResponse.getDtrcd() + "]"); // 原交易标志(ENTRCV:收款业务)
            logger.info("[dptnsrl]=[" + trdResponse.getDptnsrl() + "]"); // 原交易标志(ENTRCV:收款业务)
            logger.info("[usage]=[" + trdResponse.getUsage() + "]"); // 退款原因
            logger.info("[amt_aclamt]=[" + trdResponse.getAmt_aclamt() + "分]"); // 退款金额
            logger.info("[amt_ccycd]=[" + trdResponse.getAmt_ccycd() + "]"); // 币种，默认“CNY”
            logger.info("[state]=[" + trdResponse.getState() + "]"); // 退款结果:1退款成功;2退款失败;3退款处理中
            logger.info("[resttime]=[" + trdResponse.getResttime() + "]"); // 退款成功/失败时间(渠道通知时间)格式:YYYYMMDDHH24MISS
            logger.info("[opion]=[" + trdResponse.getOpion() + "]"); // 失败原因
            logger.info("[fdate]=[" + trdResponse.getFdate() + "]"); // 原交易日期
            logger.info("[ftime]=[" + trdResponse.getFtime() + "]"); // 原交易时间
            Map<String,String> map = new HashMap<>();
            map.put("msghd_rspmsg", trdResponse.getMsghd_rspmsg());
            map.put("srl_ptnsrl",trdResponse.getSrl_ptnsrl());
            map.put("srl_platsrl",trdResponse.getSrl_platsrl());
            map.put("trsflag",trdResponse.getTrsflag() );
            map.put("dtrcd",trdResponse.getDtrcd());
            map.put("dptnsrl", trdResponse.getDptnsrl() );
            map.put("usage",trdResponse.getUsage());
            map.put("amt_aclamt",String.valueOf(trdResponse.getAmt_aclamt()));
            map.put("amt_ccycd", trdResponse.getAmt_ccycd() );
            map.put("state",trdResponse.getState() );
            map.put("resttime",trdResponse.getResttime());
            map.put("opion", trdResponse.getOpion());
            map.put("fdate", trdResponse.getFdate() );
            map.put("ftime",trdResponse.getFtime());
            OrderRefundApply orderRefundApply = findByPtnSrl(srl_ptnsrl);
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                baseResponse.setData(map.toString());
                baseResponse.setCode("000000");
                baseResponse.setMsg("退款交易成功");
                orderRefundApply.setState(trdResponse.getState());
                mapper.update(orderRefundApply.getId(),orderRefundApply);
                return baseResponse;
            }else{
                baseResponse.setData(map.toString());
                baseResponse.setCode("000001");
                baseResponse.setMsg("退款交易失败");
                orderRefundApply.setState(trdResponse.getState());
                mapper.update(orderRefundApply.getId(),orderRefundApply);
                return baseResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退款申请导出Excel表格
     */
    public void exportPayCustomerDetail(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream("C:\\Users\\ZLZ\\Desktop\\退款.xlsx"));
            List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 1));
            List<String> arrayList = new ArrayList<>();
            for(int i = 0 ;i < data.size() ; i++){
                arrayList.add(data.get(i).toString());
            }
            List<EntryExitAccount> accountList = new ArrayList<>();
            //根据list中的数据查询到对应的数据信息
            arrayList.forEach(stringMap -> {
                String str = stringMap.substring(1,stringMap.length()-1);
                accountList.add(accountService.findByOrderNo(str));
            });
            if (accountList.size() != 0) {
                String fileName = "海利盈-" + DateUtils.stringToDate();
                String sheetName = "海利盈";
                com.example.asc.asc.util.ExcelUtil.writeExcel(response, accountList, fileName, sheetName, new EntryExitAccount());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
