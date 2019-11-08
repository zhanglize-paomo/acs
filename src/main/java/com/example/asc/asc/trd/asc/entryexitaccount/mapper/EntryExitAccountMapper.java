package com.example.asc.asc.trd.asc.entryexitaccount.mapper;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
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
}
