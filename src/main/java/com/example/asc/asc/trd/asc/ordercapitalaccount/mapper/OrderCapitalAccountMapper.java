package com.example.asc.asc.trd.asc.ordercapitalaccount.mapper;

import com.example.asc.asc.trd.asc.ordercapitalaccount.domain.OrderCapitalAccount;
import org.springframework.stereotype.Repository;

/**
 * @author zhanglize
 * @create 2019/11/19
 */
@Repository
public interface OrderCapitalAccountMapper {

    /**
     * 根据订单流水号查询对应的交易订单信息
     *
     * @param ptnSrl
     * @return
     */
    OrderCapitalAccount findByPtnSrl(String ptnSrl);

    /**
     * 添加数据到数据库中
     *
     * @param account
     * @return
     */
    int insert(OrderCapitalAccount account);
}
