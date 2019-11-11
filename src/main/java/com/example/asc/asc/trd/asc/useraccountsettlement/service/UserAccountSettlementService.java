package com.example.asc.asc.trd.asc.useraccountsettlement.service;

import com.example.asc.asc.trd.asc.useraccountsettlement.domain.UserAccountSettlement;
import com.example.asc.asc.trd.asc.useraccountsettlement.mapper.UserAccountSettlementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户申请绑定银行数据表业务层信息
 *
 * @author zhanglize
 * @create 2019/11/11
 */
@Service
public class UserAccountSettlementService {

    private UserAccountSettlementMapper mapper;
    @Autowired
    public void setMapper(UserAccountSettlementMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据用户申请表的id查询对应的对象信息
     *
     * @param accountId 用户申请表的id
     * @return
     */
    public UserAccountSettlement findAccountId(int accountId) {
       return mapper.findAccountId(accountId);
    }
}
