package com.cly.service;

import com.cly.pojo.order.OrderUser;

import java.util.List;
import java.util.Map;

public interface OrderUserService {

    /**
     * 插入新关联的用户的订单表信息
     *
     * @param id
     * @param userId
     * @return
     */
    int insert(Long id, Long userId);

    /**
     * 用户分页查找其历史订单表信息
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Map<String, Object> userPageFind(Integer page, Integer limit, Long userId);
}
