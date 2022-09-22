package com.cly.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发货时间
     */
    private Date deliverTime;

    /**
     * 货物 id
     */
    private Long goodsId;

    /**
     * 配送员 id
     */
    private Long dispatcherId;

    /**
     * 发货位置
     */
    private String deliverArea;

    /**
     * 收货位置
     */
    private String receiveArea;

    /**
     * 发货数量
     */
    private Integer number;

    /**
     * 是否到货状态 送货员送货后 确认状态 在由管理员确认历史订单
     * 0 用户下单 送货员未接单
     * 1 送货员接单 正在派送
     * 2 送货员送达 用户未确认订单
     * 3 用户确认订单 订单完成 -> 进入历史订单
     */
    private Integer state;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 商品送达时间
     */
    private Date receiveTime;
}
