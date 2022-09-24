package com.cly.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cly.pojo.order.OrderHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryMapper extends BaseMapper<OrderHistory> {
}
