package com.cly.service;

import java.util.Map;

public interface OrderDispatcherService {

    /**
     * 插入新关联的订单和派送员信息
     *
     * @param id
     * @param dispatcherId
     * @return
     */
    int insert(Long id, Long dispatcherId);

    /**
     * 获取派送员和订单关联信息
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Map<String, Object> pageFind(Integer page, Integer limit, Long userId);

    /**
     * 派送员删除其自身的订单记录
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 派送员批量删除自身订单信息记录
     *
     * @param orderIds
     * @return
     */
    void deleteByIds(Long[] orderIds);
}
