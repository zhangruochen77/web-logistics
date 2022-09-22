package com.cly.vo.order;

import lombok.Data;

@Data
public class OrderUserPageVo {

    private String id;

    private String goodsName;

    private String img;

    /**
     * 货物当前的状态
     */
    private Integer state;

    private String time;

    private String info;

}
