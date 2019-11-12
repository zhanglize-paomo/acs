package com.example.asc.asc.trd.asc.entryexitaccount.mapper;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhanglize
 * @create 2019/11/8
 */
@Repository
public interface EntryExitAccountMapper {

    /**
     * 新增入金支付-集成交易对象信息
     *
     * @param account
     * @return
     */
    int insert(EntryExitAccount account);

    /**
     * 根据根据客户方交易流水号判断该交易流水号是否存在
     *
     * @param ptnSrl  客户方交易流水号
     * @return
     */
    EntryExitAccount findByPtnSrl(String ptnSrl);

    /**
     * 根据id修改入金支付对象的信息
     *
     * @param id
     * @param account
     * @return
     */
    int update(@Param("id") int id,@Param("account") EntryExitAccount account);
}
