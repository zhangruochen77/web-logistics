package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.common.DateFormatUtils;
import com.cly.dao.OrderMapper;
import com.cly.exception.LogException;
import com.cly.feign.OrderAdminFeign;
import com.cly.feign.OrderWarehouseFeign;
import com.cly.pojo.order.Order;
import com.cly.pojo.warehouse.Goods;
import com.cly.service.OrderHistoryService;
import com.cly.service.OrderService;
import com.cly.vo.admin.UserNamePhone;
import com.cly.vo.order.OrderDispatcherPageVo;
import com.cly.vo.order.OrderManageDetailsVo;
import com.cly.vo.order.OrderManagerVo;
import com.cly.vo.order.OrderUserPageVo;
import com.cly.vo.warehouse.GoodsDispatcherPageVo;
import com.cly.vo.warehouse.GoodsNameImgVo;
import com.cly.web.ThreadLocalAdminUtils;
import com.cly.web.TokenUtils;
import com.cly.web.param.CreateOrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderWarehouseFeign orderWarehouseFeign;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private OrderAdminFeign orderAdminFeign;

    @Resource
    private ExecutorService feignExecutorService;


    /**
     * 生成用户订单
     *
     * @param params 商品信息，用户信息，下单数量，收发货地区等等信息
     * @return 订单编号
     */
    @Override
    public Long createOrder(CreateOrderParams params) {
        Order order = orderParamsToOrder(params);
        baseMapper.insert(order);

        return order.getId();
    }

    /**
     * 用户查看自己正在执行的订单
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> userPageFind(Integer page, Integer limit) {
        String token = ThreadLocalAdminUtils.get();
        Long userId = TokenUtils.getId(token);

        IPage<Order> iPage = new Page<>(page, limit);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime);
        baseMapper.selectPage(iPage, wrapper);

        List<Order> orders = iPage.getRecords();
        List<OrderUserPageVo> data = new ArrayList<>(orders.size());
        Map<String, Object> result = new HashMap<>(4);
        Map<Long, Goods> goodsMap = getGoodsInfos(orders);
        result.put("total", iPage.getTotal());
        result.put("data", data);

        orders.forEach(item -> {
            data.add(orderToOrderUserPageVo(item, goodsMap.get(item.getGoodsId())));
        });

        return result;
    }

    /**
     * 派送员分页查看订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> pageDisNewOrder(Integer page, Integer limit) {

        IPage<Order> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, new LambdaQueryWrapper<Order>().eq(Order::getState, 0));

        // 构造结果参数
        List<Order> orders = iPage.getRecords();
        List<OrderDispatcherPageVo> data = new ArrayList<>(orders.size());
        Map<String, Object> result = new HashMap<>(4);
        Map<Long, GoodsDispatcherPageVo> goodsMap = getGoodsDispatcherInfos(orders);
        result.put("total", iPage.getTotal());
        result.put("data", data);

        // 生成结果集
        orders.forEach(item -> {
            data.add(orderToOrderDispatcherPageVo(item, goodsMap.get(item.getGoodsId())));
        });

        return result;
    }

    /**
     * 分页查看当前派送员正在执行的订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> pageDisDispatcherOrder(Integer page, Integer limit) {
        IPage<Order> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, new LambdaQueryWrapper<Order>().eq(Order::getState, 1));

        // 构造结果参数
        List<Order> orders = iPage.getRecords();
        List<OrderDispatcherPageVo> data = new ArrayList<>(orders.size());
        Map<String, Object> result = new HashMap<>(4);
        Map<Long, GoodsDispatcherPageVo> goodsMap = getGoodsDispatcherInfos(orders);
        result.put("total", iPage.getTotal());
        result.put("data", data);

        // 生成结果集
        orders.forEach(item -> {
            data.add(orderToOrderDispatcherPageVoInSure(item, goodsMap.get(item.getGoodsId())));
        });

        return result;
    }

    /**
     * 确认订单送达
     *
     * @param id
     */
    @Override
    public void updateOrderSure(Long id) {

        Order order = baseMapper.selectById(id);
        // 订单送达状态
        order.setState(2);
        order.setArriveTime(new Date());

        baseMapper.updateById(order);
    }

    /**
     * 一次性确认多个订单送达
     *
     * @param orderIds
     */
    @Override
    public void listOrderSure(Long[] orderIds) {
        Order updateOrder = new Order();
        updateOrder.setState(2);
        updateOrder.setArriveTime(new Date());
        baseMapper.update(updateOrder, new LambdaQueryWrapper<Order>().in(Order::getId, orderIds));

    }

    /**
     * 用户确认订单送达
     *
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public void userSureOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        Long userId = TokenUtils.getId(ThreadLocalAdminUtils.get());

        if (!userId.equals(order.getUserId())) {
            throw new LogException("用户操作不正确");
        }

        orderHistoryService.insert(order);
        baseMapper.deleteById(orderId);

    }

    /**
     * 管理员一次性查看多个正在执行的订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> listOrder(Integer page, Integer limit) {
        IPage<Order> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, null);
        List<Order> orders = iPage.getRecords();

        // 构造商品参数 构造派送员参数
        Set<Long> goodsIds = orders.stream().map(order -> order.getGoodsId()).collect(Collectors.toSet());
        List<Long> dispatcherIds = orders.stream().map(order -> order.getDispatcherId()).distinct().collect(Collectors.toList());

        // 获取商品信息 派送员信息
        Map<Long, Goods> goodsMap = orderWarehouseFeign.listGoodsByIds(goodsIds);
        Map<Long, String> dispatcherMap = orderAdminFeign.getDispatcherNamesByIds(dispatcherIds);
        Map<String, Object> result = new HashMap<>(4);

        // 转化结果对象
        List<OrderManagerVo> data = orders.stream().map(order -> {
            Goods goods = goodsMap.get(order.getGoodsId());
            OrderManagerVo vo = new OrderManagerVo();
            vo.setImg(goods.getImg());
            vo.setState(order.getState());
            vo.setGoodsName(goods.getName());
            vo.setId(order.getId().toString());

            // 分状态判断填入时间
            if (order.getState().equals(0)) {
                vo.setTime(DateFormatUtils.dateFormat(order.getCreateTime()));
                vo.setDispatcherName("暂无");
                return vo;
            }

            vo.setDispatcherId(order.getDispatcherId().toString());
            vo.setDispatcherName(dispatcherMap.get(order.getDispatcherId()));
            if (order.getState().equals(1)) {
                vo.setTime(DateFormatUtils.dateFormat(order.getDeliverTime()));
                return vo;
            }
            if (order.getState().equals(2)) {
                vo.setTime(DateFormatUtils.dateFormat(order.getArriveTime()));
            }
            return vo;
        }).collect(Collectors.toList());

        result.put("total", iPage.getTotal());
        result.put("data", data);

        return result;
    }

    /**
     * 获取订单的执行状态下的详细信息
     * 1 填充商品数据
     * 2 填充用户数据
     * 3 有派送员信息则也填充派送员数据
     * 4 其他的订单的详细信息
     *
     * @param id 订单编号
     * @return 订单详细信息
     */
    @Override
    public OrderManageDetailsVo orderDetailById(Long id) {
        // 查订单信息
        Order order = baseMapper.selectById(id);

        // 用户确认订单信息 转化为历史订单
        if (ObjectUtils.isEmpty(order)) {
            throw new LogException("执行订单已经结束");
        }

        OrderManageDetailsVo res = new OrderManageDetailsVo();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 填充商品相关数据
            GoodsNameImgVo goodsNameImgVo = orderWarehouseFeign.getOrderNameImgById(order.getGoodsId());
            res.setGoodsId(goodsNameImgVo.getId().toString());
            res.setGoodsName(goodsNameImgVo.getName());
            res.setImg(goodsNameImgVo.getImg());
        }, feignExecutorService).thenRunAsync(() -> {
            // 填充用户相关数据
            UserNamePhone user = orderAdminFeign.getUserNamePhone(order.getUserId());
            res.setUserId(user.getId().toString());
            res.setUsername(user.getUsername());
            res.setUserPhone(user.getPhone());
        }, feignExecutorService);

        // 派送员已经接单了
        if (order.getState() > 0) {
            res.setDispatcherId(order.getDispatcherId().toString());
            res.setDispatcherName(orderAdminFeign.getDispatcherNameById(order.getDispatcherId()));
            res.setDeliverTime(DateFormatUtils.dateFormat(order.getDeliverTime()));

            // 派送员已经完成送达任务
            if (order.getState() == 2) {
                res.setArriveTime(DateFormatUtils.dateFormat(order.getArriveTime()));
            }
        }

        // 其他商品信息
        res.setState(order.getState());
        res.setNumber(order.getNumber());
        res.setId(order.getId().toString());
        res.setDeliverArea(order.getDeliverArea());
        res.setReceiveArea(order.getReceiveArea());
        res.setCreateTime(DateFormatUtils.dateFormat(order.getCreateTime()));

        future.join();
        return res;
    }


    /**
     * 转换为派送员在配送订单时的分页订单信息
     *
     * @param item
     * @param goods
     * @return
     */
    private OrderDispatcherPageVo orderToOrderDispatcherPageVoInSure(Order item, GoodsDispatcherPageVo goods) {
        OrderDispatcherPageVo vo = new OrderDispatcherPageVo();
        vo.setImg(goods.getImg());
        vo.setNumber(item.getNumber());
        vo.setGoodsName(goods.getName());
        vo.setId(item.getId().toString());
        vo.setDeliverArea(item.getDeliverArea());
        vo.setReceiveArea(item.getReceiveArea());
        vo.setDeliverArea(goods.getDeliverArea());
        vo.setTime(DateFormatUtils.dateFormat(item.getDeliverTime()));  // 配送时间

        return vo;
    }

    /**
     * 派送员接单
     *
     * @param id
     * @return
     */
    @Override
    public boolean orderReceive(Long id) {
        String token = ThreadLocalAdminUtils.get();
        Long dispatcherId = TokenUtils.getId(token);

        Order order = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(order)) {
            throw new LogException("订单不存在");
        }

        if (!order.getState().equals(0)) {
            throw new LogException("订单已经被接收");
        }

        order.setState(1);
        order.setDeliverTime(new Date());
        order.setDispatcherId(dispatcherId);

        // cas 防止中途被接单
        return 1 == baseMapper.update(order, new LambdaQueryWrapper<Order>().eq(Order::getState, 0).eq(Order::getId, order.getId()));
    }


    /**
     * 一次性接多个单
     *
     * @param orderIds
     * @return
     */
    @Override
    public int listOrderReceive(Long[] orderIds) {
        String token = ThreadLocalAdminUtils.get();
        Long dispatcherId = TokenUtils.getId(token);

        List<Order> orders = baseMapper.selectBatchIds(Arrays.asList(orderIds));

        // 1 先对订单进行过滤 当其还在为新订单 state = 0 时，才能进行下单操作 --> filter
        // 2 再对下订单进行操作 更新订单下单时间 发送时间以及派送员信息 --> map
        // 3 对数据库进行更新操作 并且使用 cas 算法 仅仅一次 因为当 state 不为 0 时则证明订单已经被其他派送员给抢单了 mapToInt
        // 4 统计此次下单成功的数量 返回给派送员 让其知道完成了几个订单 sum
        return orders.stream()
                .filter(order -> order.getState().equals(0)).map(order -> {
                    order.setState(1);
                    order.setDeliverTime(new Date());
                    order.setDispatcherId(dispatcherId);
                    return order;
                }).mapToInt(order -> baseMapper.update(order, new LambdaQueryWrapper<Order>().eq(Order::getState, 0).eq(Order::getId, order.getId())))
                .sum();

    }


    /**
     * 转化 order 对象为派送员分页查看对象
     *
     * @param item
     * @param goods
     * @return
     */
    private OrderDispatcherPageVo orderToOrderDispatcherPageVo(Order item, GoodsDispatcherPageVo goods) {
        OrderDispatcherPageVo vo = new OrderDispatcherPageVo();
        vo.setImg(goods.getImg());
        vo.setNumber(item.getNumber());
        vo.setGoodsName(goods.getName());
        vo.setId(item.getId().toString());
        vo.setDeliverArea(item.getDeliverArea());
        vo.setReceiveArea(item.getReceiveArea());
        vo.setDeliverArea(goods.getDeliverArea());
        vo.setTime(DateFormatUtils.dateFormat(item.getCreateTime()));  // 下单时间

        return vo;
    }


    /**
     * 远程调用获取商品信息
     *
     * @param orders
     * @return
     */
    private Map<Long, Goods> getGoodsInfos(List<Order> orders) {
        // 使用 set 的原因 -> order 表和 goods 表属于是 一对多的关系 所以存储可能会重复
        Set<Long> goodsIds = new HashSet<>(orders.size() << 1);

        orders.forEach(item -> {
            goodsIds.add(item.getGoodsId());
        });

        // 远程调用获取商品信息
        return orderWarehouseFeign.listGoodsByIds(goodsIds);
    }


    /**
     * 远程调用获取商品信息
     *
     * @param orders
     * @return
     */
    private Map<Long, GoodsDispatcherPageVo> getGoodsDispatcherInfos(List<Order> orders) {
        // 使用 set 的原因 -> order 表和 goods 表属于是 一对多的关系 所以存储可能会重复
        Set<Long> goodsIds = new HashSet<>(orders.size() << 1);

        orders.forEach(item -> {
            goodsIds.add(item.getGoodsId());
        });

        // 远程调用获取商品信息
        return orderWarehouseFeign.listGoodsDispatcherPageVoByIds(goodsIds);
    }

    /**
     * 订单对象转化为分页状态展示下的商品对象
     *
     * @param item
     * @param goods
     * @return
     */
    private OrderUserPageVo orderToOrderUserPageVo(Order item, Goods goods) {
        OrderUserPageVo vo = new OrderUserPageVo();

        int state = item.getState();

        vo.setId(item.getId().toString());
        vo.setGoodsName(goods.getName());
        vo.setImg(goods.getImg());
        vo.setState(item.getState());

        // 用户下单了 还未
        if (state == 0) {
            vo.setTime(DateFormatUtils.dateFormat(item.getCreateTime()));
            vo.setInfo("您创建了订单");
            return vo;
        }

        if (state == 1) {
            vo.setTime(DateFormatUtils.dateFormat(item.getDeliverTime()));
            vo.setInfo("派送员正在派送中");
            return vo;
        }

        if (state == 2) {
            vo.setTime(DateFormatUtils.dateFormat(item.getArriveTime()));
            vo.setInfo("订单已经送达,请您确认订单");
        }

        return vo;
    }

    /**
     * 订单产生转化为订单
     *
     * @param params
     * @return
     */
    private Order orderParamsToOrder(CreateOrderParams params) {
        Order order = new Order();

        // 用户下单 未接收
        order.setState(0);
        order.setCreateTime(new Date());
        order.setNumber(params.getNumber());
        order.setUserId(params.getUserId());
        order.setGoodsId(params.getGoodsId());
        order.setReceiveArea(params.getReceiveArea());

        return order;
    }

}
