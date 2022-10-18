package com.cly.vo.order;

import lombok.Data;

/**
 * 用户查看一个正真执行的订单的详细信息
 * 包含了订单的创建，接单，送单，送达等时间
 * 订单商品的信息
 * 收货人信息
 * 派送员信息
 * 收发地址等等信息
 */
@Data
public class OrderManageDetailsVo {

    private String id;

    private String createTime;

    private String deliverTime;

    private String arriveTime;

    private String goodsId;

    private String goodsName;

    private String img;

    private String dispatcherId;

    private String dispatcherName;

    private String userId;

    private String username;

    private String userPhone;

    private Integer number;

    private String deliverArea;

    private String receiveArea;

    /**
     * 订单目前状态
     */
    private Integer state;
}
