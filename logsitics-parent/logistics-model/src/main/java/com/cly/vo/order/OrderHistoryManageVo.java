package com.cly.vo.order;

import lombok.Data;

@Data
public class OrderHistoryManageVo {

    private String id;

    private String createTime;

    private String deliverTime;

    private String arriveTime;

    private String receiveTime;

    private String dispatcherId;

    private String dispatcherName;

    private String goodsId;

    private String goodsName;

    private String img;

    private String deliverArea;

    private String receiveArea;

    private String userId;

    private String userName;

    private Integer deleteCount;

    private Integer number;

}
