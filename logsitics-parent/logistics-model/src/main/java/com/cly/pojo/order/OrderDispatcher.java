package com.cly.pojo.order;

import lombok.Data;

/**
 * 派送员是否删除订单信息表 关联表
 */
@Data
public class OrderDispatcher {

    private Long id;

    private Long dispatcherId;

    private Long orderId;

}
