package com.example.asc.asc.trd.asc.users.mapper;

import com.example.asc.asc.trd.asc.users.domain.Users;
import org.springframework.stereotype.Repository;

/**
 * @author zhanglize
 * @create 2019/11/14
 */
@Repository
public interface UsersMapper {
    /**
     * 根据appid查询到对应的人员信息
     *
     * @param appId
     * @return
     */
    Users findAppId(String appId);
}
