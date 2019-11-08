package com.example.asc.asc.trd.asc.useraccount.mapper;

import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import org.springframework.stereotype.Repository;

/**
 * 用户申请信息Mapper
 *
 * @author zhanglize
 * @create 2019/11/6
 */
@Repository
public interface UserAccountMapper {

    /**
     * 根据资金账号查询到对应的资金账户信息
     *
     * @param subno
     * @return
     */
    UserAccount findBySubNo(String subno);

}
