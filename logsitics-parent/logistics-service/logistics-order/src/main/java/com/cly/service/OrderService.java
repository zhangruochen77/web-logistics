package com.cly.service;

import com.cly.vo.order.OrderManageDetailsVo;
import com.cly.web.param.CreateOrderParams;

import java.util.List;
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

    /**
     * 用户确认订单送达
     *
     * @param orderId
     * @return
     */
    void userSureOrder(Long orderId);

    /**
     * 管理员一次性查看多个正在执行的订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> listOrder(Integer page, Integer limit);

    /**
     * 获取订单的当前状态下的详细信息
     *
     * @param id 订单编号
     * @return 订单详细信息
     */
    OrderManageDetailsVo orderDetailById(Long id);

}
