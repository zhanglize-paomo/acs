package com.example.asc.asc.trd.asc.orderrefundapply.mapper;

import com.example.asc.asc.trd.asc.orderrefundapply.domain.OrderRefundApply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhanglize
 * @create 2020/1/3
 */
@Repository
public interface OrderRefundApplyMapper {

    /**
     * 新增退款申请数据信息
     *
     * @param apply
     * @return
     */
    int insert(OrderRefundApply apply);

    /**
     * 根据合作方交易流水号查询到对应的退款订单信息
     *
     * @param srl_ptnsrl
     * @return
     */
    OrderRefundApply findByPtnSrl(String srl_ptnsrl);

    /**
     * 根据id修改对应的退款订单信息
     *
     * @param id
     * @param orderRefundApply
     * @return
     */
    int update(@Param(value = "id") Long id,@Param(value = "orderRefundApply") OrderRefundApply orderRefundApply);
}
