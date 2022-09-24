package com.cly.service;

import com.cly.pojo.order.Order;

import java.util.Map;

public interface OrderHistoryService {

    /**
     * 创建历史订单信息
     *
     * @param order
     * @return
     */
    int insert(Order order);


    /**
     * 用户查看历史订单
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> userPageFind(Integer page, Integer limit);


    /**
     * 派送员查看其历史订单
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> pageFind(Integer page, Integer limit);

    /**
     * 对历史订单的删除记录 + 1 -> 用户和派送员可以操作
     *
     * @param id
     */
    void addRemoveCount(Long id);


    /**
     * 一次删除多条历史订单记录
     *
     * @param orderIds
     */
    void addRemoveCount(Long[] orderIds);
}
