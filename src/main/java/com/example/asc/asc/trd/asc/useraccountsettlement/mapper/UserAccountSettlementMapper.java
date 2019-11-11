package com.example.asc.asc.trd.asc.useraccountsettlement.mapper;

import com.example.asc.asc.trd.asc.useraccountsettlement.domain.UserAccountSettlement;
import org.springframework.stereotype.Repository;

/**
 * 用户申请绑定银行数据Mapper
 *
 * @author zhanglize
 * @create 2019/11/11
 */
@Repository
public interface UserAccountSettlementMapper {

    /**
     * 根据用户申请表的id查询对应的对象信息
     *
     * @param accountId 用户申请表的id
     * @return
     */
    UserAccountSettlement findAccountId(int accountId);
}
