package com.cly.vo.car;

import lombok.Data;

@Data
public class CarDispatcherVo {

    /**
     * 主键
     */
    private String id;

    /**
     * 车牌号码
     */
    private String carId;

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 开始使用时间
     */
    private String startUseTime;

    /**
     * 使用时长
     */
    private Integer useTime;

}
