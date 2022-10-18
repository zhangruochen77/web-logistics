package com.cly.vo.order;

import lombok.Data;

@Data
public class OrderManagerVo {

    private String id;

    private String goodsName;

    private String img;

    private Integer state;

    private String time;

    private String dispatcherId;

    private String dispatcherName;

}
