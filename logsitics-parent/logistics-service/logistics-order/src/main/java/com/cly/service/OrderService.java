package com.cly.service;

import com.cly.web.param.CreateOrderParams;

import java.util.Map;

public interface OrderService {

    /**
     * 生成用户订单
     *
     * @param params 商品信息，用户信息，下单数量，收发货地区等等信息
     * @return 订单编号
     */
    Long createOrder(CreateOrderParams params);

    /**
     * 用户查看自己正在执行的订单
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> userPageFind(Integer page, Integer limit);

    /**
     * 分页查看新订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> pageDisNewOrder(Integer page, Integer limit);

    /**
     * 派送员接单
     *
     * @param id
     * @return
     */
    boolean orderReceive(Long id);

    /**
     * 一次性接多个单
     *
     * @param orderIds
     * @return
     */
    int listOrderReceive(Long[] orderIds);

    /**
     * 分页查看当前派送员正在执行的订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> pageDisDispatcherOrder(Integer page, Integer limit);

    /**
     * 确认订单送达
     *
     * @param id
     */
    void updateOrderSure(Long id);

    /**
     * 一次性确认多个订单送达
     *
     * @param orderIds
     */
    void listOrderSure(Long[] orderIds);
}
