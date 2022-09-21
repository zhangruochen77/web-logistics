package com.cly.vo.warehouse;

import lombok.Data;

/**
 * 用户可以看到的其他人完成订单的状态
 */
@Data
public class OrderUserAboutInfo {

    /**
     * 下订单的用户名称
     */
    private String username;

    /**
     * 下订单的时间
     */
    private String time;

    /**
     * 订单现在的情况
     */
    private String action;

}
