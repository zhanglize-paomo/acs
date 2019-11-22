package com.example.asc.asc.trd.asc.cloudflashoverorder.mapper;

import com.example.asc.asc.trd.asc.cloudflashoverorder.domain.CloudFlashoverOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhanglize
 * @create 2019/11/22
 */
@Repository
public interface CloudFlashoverOrderMapper {

    /**
     * 新增云闪付支付对象信息
     *
     * @param order
     * @return
     */
    int insert(@Param("order") CloudFlashoverOrder order);

    /**
     * 根据id查询对应的云闪付信息
     *
     * @param id
     * @return
     */
    CloudFlashoverOrder findById(Long id);
}
