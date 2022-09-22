package com.cly.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cly.pojo.order.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
