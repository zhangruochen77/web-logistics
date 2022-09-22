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

}
