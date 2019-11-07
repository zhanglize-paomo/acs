package com.example.asc.asc.trd.asc.applydepositaccount.mapper;

import com.example.asc.asc.trd.asc.applydepositaccount.domain.ApplyDepositAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanglize
 * @create 2019/11/6
 */
@Repository
public interface ApplyDepositAccountMapper {

    /**
     * 根据资金账户以及交易日期查询出金申请记录
     *
     * @param cltaccSubno 资金账户
     * @param msghdTrdt   交易日期
     * @return
     */
    List<ApplyDepositAccount> queryCount(@Param("cltaccSubno") String cltaccSubno, @Param("msghdTrdt") String msghdTrdt);

    /**
     * 新增数据到出金申请表中信息
     *
     * @param account  出金申请对象信息
     * @return
     */
    int insert(ApplyDepositAccount account);
}
