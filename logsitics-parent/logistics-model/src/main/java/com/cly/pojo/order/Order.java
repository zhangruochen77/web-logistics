package com.cly.pojo.order;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Base {

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
     */
    private Integer state;
}
