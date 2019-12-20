package com.example.asc.asc.trd.asc.withdraw;

import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.service.UserAccountService;
import com.example.asc.asc.trd.common.BaseResponse;
import com.example.asc.asc.trd.common.DateCommonUtils;
import com.example.asc.asc.trd.common.FileConfigure;
import com.example.asc.asc.util.MoneyUtils;
import com.example.asc.asc.util.StringUtil;
import com.trz.netwk.api.system.TrdMessenger;
import com.trz.netwk.api.trd.TrdT1018Request;
import com.trz.netwk.api.trd.TrdT1018Response;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * 查询可 T0/T1 出金额度[T1018]业务层
 *
 * @author zhanglize
 * @create 2019/11/4
 */
@Service
public class WithDrawService {

    private static final String TAG = "{出金额度}-";
    private static final Logger logger = LoggerFactory.getLogger(WithDrawService.class);
    private UserAccountService userAccountService;

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * 查询可 T0/T1 出金额度
     *
     * @return
     */
    public BaseResponse withDraw(HttpServletRequest req, HttpServletResponse resp) {
        BaseResponse response = new BaseResponse();
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            //交易日期
            String msghd_trdt = DateCommonUtils.judgeDateFormat(req.getParameter("msghd_trdt"));
            //合作方编号
            String PtnCd = "HLYI2019";
            //托管方编号
            String BkCd = "ZXYH0001";
            /** 资金账号 */
            String cltacc_subno = req.getParameter("subNo");
            String flag = req.getParameter("flag");
            //根据资金账号查询户名信息
            UserAccount userAccount = userAccountService.findBySubNo(cltacc_subno);
            if (userAccount == null) {
                response.setCode("CJ300");
                response.setMsg("资金账号不存在,请核对资金账户信息");
                response.setData(null);
                return response;
            }
            /** 户名 */
            String cltacc_cltnm = userAccount.getName();
            //加载配置文件信息
            FileConfigure.getFileConfigure(cltacc_subno);
            // 2. 实例化交易对象
            TrdT1018Request trdRequest = new TrdT1018Request();
            trdRequest.setMsghd_trdt(msghd_trdt);
            trdRequest.setCltacc_subno(cltacc_subno);
            trdRequest.setCltacc_cltnm(cltacc_cltnm);
            trdRequest.setMsghd_ptncd(PtnCd);
            trdRequest.setMsghd_bkcd(BkCd);
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
            TrdT1018Response trdResponse = new TrdT1018Response(respMsg);
            logger.info(TAG + "响应报文[" + trdResponse.getResponsePlainText() + "]");
            // 交易成功 000000
            if ("000000".equals(trdResponse.getMsghd_rspcode())) {
                response = getTreeMap(trdResponse, cltacc_subno, flag);
            } else {
                response = getTreeMap(trdResponse, cltacc_subno, flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 获取返回消息报文信息
     *
     * @param trdResponse
     * @param cltacc_subno
     * @param flag
     * @return
     */
    private BaseResponse getTreeMap(TrdT1018Response trdResponse, String cltacc_subno, String flag) {
        //获取请求报文信息
        BaseResponse response = new BaseResponse();
        Map<String, String> treeMap = new TreeMap<>();
        if (StringUtils.isEmpty(flag)) {
            treeMap.put("t0amt_ctamta00", MoneyUtils.convertPart(trdResponse.getT0amt_ctamta00()));
        } else {
            treeMap.put("t0amt_ctamta00", MoneyUtils.convertPart(trdResponse.getT0amt_ctamta00()));
            treeMap.put("t1amt_ctamta00", MoneyUtils.convertPart(trdResponse.getT1amt_ctamta00()));
        }
        treeMap.put("subNo", cltacc_subno);
        response.setCode(trdResponse.getMsghd_rspcode());
        response.setMsg(trdResponse.getMsghd_rspmsg());
        response.setData(JSONObject.fromObject(treeMap));
        return response;
    }


}
