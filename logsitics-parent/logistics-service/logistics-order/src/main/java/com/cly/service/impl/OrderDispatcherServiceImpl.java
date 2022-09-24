package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.OrderDispatcherMapper;
import com.cly.pojo.order.OrderDispatcher;
import com.cly.service.OrderDispatcherService;
import com.cly.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderDispatcherServiceImpl extends ServiceImpl<OrderDispatcherMapper, OrderDispatcher>
        implements OrderDispatcherService {


    @Autowired
    private OrderHistoryService orderHistoryService;

    /**
     * 插入新关联的订单和派送员信息
     *
     * @param id
     * @param dispatcherId
     * @return
     */
    @Override
    public int insert(Long id, Long dispatcherId) {
        OrderDispatcher od = new OrderDispatcher();
        od.setOrderId(id);
        od.setDispatcherId(dispatcherId);
        return baseMapper.insert(od);
    }

    /**
     * 获取派送员和订单关联信息
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> pageFind(Integer page, Integer limit, Long userId) {
        IPage<OrderDispatcher> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, new LambdaQueryWrapper<OrderDispatcher>().eq(OrderDispatcher::getDispatcherId, userId));

        Map<String, Object> map = new HashMap<>(4);
        List<Long> collect = iPage.getRecords().stream().map(item -> item.getOrderId()).collect(Collectors.toList());
        map.put("data", collect);
        map.put("total", iPage.getTotal());

        return map;
    }

    /**
     * 派送员删除其自身的订单记录
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        baseMapper.delete(new LambdaQueryWrapper<OrderDispatcher>().eq(OrderDispatcher::getOrderId, id));
        orderHistoryService.addRemoveCount(id);
    }

    /**
     * 派送员批量删除自身订单信息记录
     *
     * @param orderIds
     * @return
     */
    @Override
    public void deleteByIds(Long[] orderIds) {
        baseMapper.delete(new LambdaUpdateWrapper<OrderDispatcher>().in(OrderDispatcher::getOrderId, Arrays.asList(orderIds)));

        // TODO: 2022/9/24 可以使用多线程开启优化操作 结果直接返回 新开线程进行执行操作
        orderHistoryService.addRemoveCount(orderIds);
    }
}
