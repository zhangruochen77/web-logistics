package com.cly.vo.order;

import lombok.Data;

/**
 * 派送员查看完成的历史订单信息
 */
@Data
public class OrderDispatcherHistoryPageVo {

    private String id;

    private String goodsName;

    private String img;

    private String createTime;

    private String endTime;

    private String deliverArea;

    private String receiveArea;

    private Integer number;

}
