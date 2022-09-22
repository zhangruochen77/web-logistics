package com.cly.vo.order;

import lombok.Data;

/**
 * 派送员所查看的未接收的订单分页类
 */
@Data
public class OrderDispatcherPageVo {

    private String id;

    private String goodsName;

    private String img;

    private String time;

    private String deliverArea;

    private String receiveArea;

    private Integer number;

}
