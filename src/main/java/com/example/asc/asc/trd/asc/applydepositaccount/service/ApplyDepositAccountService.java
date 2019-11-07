package com.example.asc.asc.trd.asc.applydepositaccount.service;

import com.example.asc.asc.trd.asc.applydepositaccount.domain.ApplyDepositAccount;
import com.example.asc.asc.trd.asc.applydepositaccount.mapper.ApplyDepositAccountMapper;
import com.example.asc.asc.util.DateUtils;
import com.example.asc.asc.util.SnowflakeIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 出金申请记录信息业务层信息
 *
 * @author zhanglize
 * @create 2019/11/6
 */
@Service
public class ApplyDepositAccountService {

    private ApplyDepositAccountMapper mapper;

    @Autowired
    public void setMapper(ApplyDepositAccountMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据资金账户以及交易日期查询出金申请记录
     *
     * @param cltaccSubno 资金账户
     * @param msghdTrdt   交易日期
     * @return
     */
    public List<ApplyDepositAccount> queryCount(String cltaccSubno, String msghdTrdt) {
        return mapper.queryCount(cltaccSubno, msghdTrdt);
    }

    /**
     * 新增数据到出金申请表中信息
     *
     * @param account 出金申请对象信息
     * @return
     */
    public int insert(ApplyDepositAccount account) {
        account.setId(new SnowflakeIdUtils().nextId());
        account.setCreateTime(DateUtils.stringToDate());
        return mapper.insert(account);
    }

    /**
     * 根据合作方交易流水号查询
     *
     * @param srlPtnsrl
     * @return
     */
    public ApplyDepositAccount querySrlPtnsrl(String srlPtnsrl) {
        return mapper.querySrlPtnsrl(srlPtnsrl);
    }

    /**
     * 修改出金申请表数据信息
     *
     *
     * @param id
     * @param account
     * @return
     */
    public int update(Long id, ApplyDepositAccount account) {
        return mapper.update(id,account);
    }
}
