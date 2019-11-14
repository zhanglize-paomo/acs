package com.example.asc.asc.trd.asc.users.service;

import com.example.asc.asc.trd.asc.users.domain.Users;
import com.example.asc.asc.trd.asc.users.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务层
 *
 * @author zhanglize
 * @create 2019/11/14
 */
@Service
public class UsersService {

    private UsersMapper mapper;
    @Autowired
    public void setMapper(UsersMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据appid查询到对应的人员信息
     *
     * @param appId
     * @return
     */
    public Users findAppId(String appId) {
        return mapper.findAppId(appId);
    }

    public Users findUserId(int userId) {
        return null;
    }
}
