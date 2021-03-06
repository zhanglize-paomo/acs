package com.example.asc.asc.trd.asc.entryexitaccount.mapper;

import com.example.asc.asc.trd.asc.entryexitaccount.domain.EntryExitAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    int insert(@Param("account") EntryExitAccount account);

    /**
     * 根据根据客户方交易流水号判断该交易流水号是否存在
     *
     * @param ptnSrl  客户方交易流水号
     * @return
     */
    EntryExitAccount findByPtnSrl(String ptnSrl);

    /**
     * 根据根据平台订单号查询到对应得订单信息
     *
     * @param orderNo 平台订单号
     * @return
     */
    EntryExitAccount findByOrderNo(String orderNo);

    /**
     * 根据id修改入金支付对象的信息
     *
     * @param id
     * @param account
     * @return
     */
    int update(@Param("id") int id,@Param("account") EntryExitAccount account);

    /**
     * 查询订单不同状态的数据信息
     *
     * @param status
     */
    List<EntryExitAccount> findByStatus(String status);

    /**
     * 根据订单时间以及金额查询到对应的订单信息
     *
     * @param createdAt
     * @param money
     * @return
     */
    EntryExitAccount findByCreateMoney(@Param("createdAt") String createdAt,@Param("money")  String money);
}
