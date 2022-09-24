package com.cly.pojo.order;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistory extends Base {

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单发货时间
     */
    private Date deliverTime;

    /**
     * 订单完成时间
     */
    private Date receiveTime;

    /**
     * 配送员 id
     */
    private Long dispatcherId;

    /**
     * 货物 id
     */
    private Long goodsId;

    /**
     * 发送地址
     */
    private String deliverArea;

    /**
     * 收货地址
     */
    private String receiveArea;

    /**
     * 货物数量
     */
    private Integer number;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单送达时间
     */
    private Date arriveTime;

    /**
     * 派送员和用户删除该订单信息的情况
     */
    private Integer deleteCount;

}
