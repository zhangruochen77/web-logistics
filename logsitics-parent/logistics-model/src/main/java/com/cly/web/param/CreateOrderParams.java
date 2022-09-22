package com.cly.web.param;

import lombok.Data;

/**
 * 生成商品所需参数类
 */
@Data
public class CreateOrderParams {

    /**
     * 商品 id
     */
    private Long goodsId;

    /**
     * 收货地区
     */
    private String receiveArea;

    /**
     * 发货地区
     */
    private String deliverArea;

    /**
     * 下单商品数量
     */
    private Integer number;

    /**
     * 用户id
     */
    private Long userId;

}
