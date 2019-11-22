package com.example.asc.asc.trd.asc.cloudflashoverorder.service;

import com.example.asc.asc.trd.asc.cloudflashoverorder.domain.CloudFlashoverOrder;
import com.example.asc.asc.trd.asc.cloudflashoverorder.mapper.CloudFlashoverOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 云闪付支付信息业务层
 *
 * @author zhanglize
 * @create 2019/11/22
 */
@Service
public class CloudFlashoverOrderService {

    private CloudFlashoverOrderMapper mapper;

    @Autowired
    public void setMapper(CloudFlashoverOrderMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 新增云闪付支付对象信息
     *
     * @param order
     * @return
     */
    public int insert(CloudFlashoverOrder order) {
        return mapper.insert(order);
    }

    /**
     * 根据id查询对应的云闪付信息
     *
     * @param id
     * @return
     */
    public CloudFlashoverOrder findById(Long id) {
        return mapper.findById(id);
    }
}
