package com.example.asc.asc.trd.asc.useraccount.service;

import com.example.asc.asc.trd.asc.useraccount.domain.UserAccount;
import com.example.asc.asc.trd.asc.useraccount.mapper.UserAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户申请信息业务层
 *
 * @author zhanglize
 * @create 2019/11/6
 */
@Service
public class UserAccountService {
    @Autowired
    private UserAccountMapper mapper;

    /**
     * 根据资金账号查询到对应的资金账户信息
     *
     * @param subno
     * @return
     */
    public UserAccount findBySubNo(String subno) {
        return mapper.findBySubNo(subno);
    }

    /**
     * 根据人员id以及状态查询到对应的用户申请信息
     *
     * @param userId      人员id
     * @param status  状态
     * @return
     */
    public UserAccount findByUserId(int userId, String status) {
        return mapper.findByUserId(userId,status);
    }
}
