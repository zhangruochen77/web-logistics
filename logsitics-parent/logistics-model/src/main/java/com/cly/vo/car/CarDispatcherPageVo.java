package com.cly.vo.car;

import lombok.Data;

/**
 * 派送员分页查看未分配车辆的信息
 */
@Data
public class CarDispatcherPageVo {

    private String id;

    private String name;

    private String carId;

    private String startUseTime;

    private Integer useTime;

}
