package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.OrderUserMapper;
import com.cly.pojo.order.OrderUser;
import com.cly.service.OrderUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderUserServiceImpl extends ServiceImpl<OrderUserMapper, OrderUser>
        implements OrderUserService {


    /**
     * 插入新关联的用户的订单表信息
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public int insert(Long id, Long userId) {
        OrderUser orderUser = new OrderUser();
        orderUser.setOrderId(id);
        orderUser.setUserId(userId);
        return baseMapper.insert(orderUser);
    }

    /**
     * 用户分页查找其历史订单表信息
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> userPageFind(Integer page, Integer limit, Long userId) {
        IPage<OrderUser> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, new LambdaQueryWrapper<OrderUser>().eq(OrderUser::getUserId, userId));

        Map<String, Object> map = new HashMap<>(4);
        map.put("data", iPage.getRecords());
        map.put("total", iPage.getTotal());

        return map;
    }
}
