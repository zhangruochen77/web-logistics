package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.common.DateFormatUtils;
import com.cly.dao.OrderHistoryMapper;
import com.cly.feign.OrderWarehouseFeign;
import com.cly.pojo.order.Order;
import com.cly.pojo.order.OrderHistory;
import com.cly.pojo.order.OrderUser;
import com.cly.pojo.warehouse.Goods;
import com.cly.service.OrderDispatcherService;
import com.cly.service.OrderHistoryService;
import com.cly.service.OrderUserService;
import com.cly.vo.order.OrderDispatcherHistoryPageVo;
import com.cly.vo.order.OrderUserHistoryPageVo;
import com.cly.web.ThreadLocalAdminUtils;
import com.cly.web.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderHistoryServiceImpl extends ServiceImpl<OrderHistoryMapper, OrderHistory>
        implements OrderHistoryService {


    @Autowired
    private OrderUserService orderUserService;

    @Autowired
    private OrderDispatcherService orderDispatcherService;

    @Autowired
    private OrderWarehouseFeign orderWarehouseFeign;

    /**
     * 创建历史订单信息
     *
     * @param order
     * @return
     */
    @Override
    public int insert(Order order) {
        OrderHistory history = orderToOrderHistory(order);
        orderDispatcherService.insert(order.getId(), order.getDispatcherId());
        orderUserService.insert(order.getId(), order.getUserId());
        return baseMapper.insert(history);
    }

    /**
     * 用户分页查看历史订单
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> userPageFind(Integer page, Integer limit) {
        String token = ThreadLocalAdminUtils.get();
        Long userId = TokenUtils.getId(token);

        // 调用用户和商品的关联信息
        Map<String, Object> orderUsersMap = orderUserService.userPageFind(page, limit, userId);
        List<OrderUser> orderUsers = (List<OrderUser>) orderUsersMap.get("data");
        List<Long> orderIds = orderUsers.stream().map(orderUser -> orderUser.getOrderId()).collect(Collectors.toList());

        // 获取到历史订单信息 提取商品id信息
        List<OrderHistory> orderHistories = baseMapper.selectBatchIds(orderIds);
        List<OrderUserHistoryPageVo> data = new ArrayList<>(orderHistories.size());
        Set<Long> goodsIds = new HashSet<>(orderHistories.size() << 1);
        orderHistories.forEach(item -> {
            goodsIds.add(item.getGoodsId());
        });


        // 远程调用获取商品信息
        Map<Long, Goods> goodsMap = orderWarehouseFeign.listGoodsByIds(goodsIds);

        // 封装结果集
        orderHistories.forEach(item -> {
            data.add(orderHistoryToOrderUserPageVo(item, goodsMap.get(item.getGoodsId())));
        });

        orderUsersMap.put("data", data);

        return orderUsersMap;
    }

    /**
     * 派送员查看其历史订单
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> pageFind(Integer page, Integer limit) {
        Long userId = TokenUtils.getId(ThreadLocalAdminUtils.get());

        // 获取派送员订单历史关联表
        Map<String, Object> orderDispatchMap = orderDispatcherService.pageFind(page, limit, userId);

        // 获取订单表信息
        List<Long> orderIds = (List<Long>) orderDispatchMap.get("data");
        List<OrderHistory> orderHistories = baseMapper.selectBatchIds(orderIds);
        Set<Long> goodsIds = new HashSet<>(orderIds.size() << 1);

        orderHistories.forEach(item -> {
            goodsIds.add(item.getGoodsId());
        });

        // 获取商品信息
        Map<Long, Goods> goodsMap = orderWarehouseFeign.listGoodsByIds(goodsIds);

        // 转化为派送员历史状态下的分页查看对象
        List<OrderDispatcherHistoryPageVo> data = orderHistories.stream().map(item -> {
            Goods goods = goodsMap.get(item.getGoodsId());
            OrderDispatcherHistoryPageVo vo = new OrderDispatcherHistoryPageVo();
            vo.setImg(goods.getImg());
            vo.setNumber(item.getNumber());
            vo.setGoodsName(goods.getName());
            vo.setId(item.getId().toString());
            vo.setDeliverArea(item.getDeliverArea());
            vo.setReceiveArea(item.getReceiveArea());
            vo.setCreateTime(DateFormatUtils.dateFormat(item.getCreateTime()));
            vo.setEndTime(DateFormatUtils.dateFormat(item.getReceiveTime()));
            return vo;
        }).collect(Collectors.toList());
        orderDispatchMap.put("data", data);

        return orderDispatchMap;
    }


    /**
     * 对历史订单的删除记录 + 1 -> 用户和派送员可以操作
     * 使用 cas 保证操作能够成功
     *
     * @param id
     */
    @Override
    public void addRemoveCount(Long id) {
        int row = 0;
        do {
            OrderHistory history = baseMapper.selectById(id);
            Integer oldCount = history.getDeleteCount();
            history.setDeleteCount(oldCount + 1);
            row = baseMapper.update(history, new LambdaUpdateWrapper<OrderHistory>().eq(OrderHistory::getId, id).eq(OrderHistory::getDeleteCount, oldCount));
        } while (0 == row);

    }

    /**
     * 一次删除多条订单记录操作
     *
     * @param orderIds
     */
    @Override
    public void addRemoveCount(Long[] orderIds) {
        for (Long orderId : orderIds) {
            addRemoveCount(orderId);
        }
    }

    /**
     * 转换历史订单为用户查看的历史订单对象
     *
     * @param item
     * @return
     */
    private OrderUserHistoryPageVo orderHistoryToOrderUserPageVo(OrderHistory item, Goods goods) {

        OrderUserHistoryPageVo vo = new OrderUserHistoryPageVo();
        vo.setId(item.getId().toString());
        vo.setGoodsName(goods.getName());
        vo.setImg(goods.getImg());
        vo.setTime(DateFormatUtils.dateFormat(item.getReceiveTime()));
        vo.setNumber(item.getNumber());

        return vo;
    }

    /**
     * order 对象转换为 orderHistory 对象
     *
     * @param order
     * @return
     */
    private OrderHistory orderToOrderHistory(Order order) {
        OrderHistory history = new OrderHistory();
        history.setDeleteCount(0);   // 默认值 没有删除记录
        history.setId(order.getId());
        history.setReceiveTime(new Date());  // 确认收货时间
        history.setNumber(order.getNumber());
        history.setUserId(order.getUserId());
        history.setGoodsId(order.getGoodsId());
        history.setArriveTime(order.getArriveTime());
        history.setCreateTime(order.getCreateTime());
        history.setDeliverArea(order.getDeliverArea());
        history.setReceiveArea(order.getReceiveArea());
        history.setDeliverTime(order.getDeliverTime());
        history.setDispatcherId(order.getDispatcherId());

        return history;
    }
}
