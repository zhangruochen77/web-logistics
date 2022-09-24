package com.cly.pojo.order;

import lombok.Data;

/**
 * 用户是否删除订单信息 关联表
 */
@Data
public class OrderUser {

    private Long id;

    private Long userId;

    private Long orderId;
}
