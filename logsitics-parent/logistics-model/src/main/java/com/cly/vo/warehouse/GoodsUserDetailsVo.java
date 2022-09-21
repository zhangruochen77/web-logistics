package com.cly.vo.warehouse;

import lombok.Data;

import java.util.List;

/**
 * 用户查看商品详细信息页面
 */
@Data
public class GoodsUserDetailsVo {

    private String id;

    private String name;

    private Integer sold;

    private String description;

    private String img;

    private String price;

    /**
     * 商品当前所在地址
     */
    private String address;

    /**
     * 剩余数量
     */
    private Integer number;

    /**
     * 该商品产生的订单信息
     */
    private List<OrderUserAboutInfo> orderInfos;

}
