package com.cly.vo.car;

import lombok.Data;

/**
 * 派送员是否申请车辆信息相关类
 */
@Data
public class CarRequireVo {

    private String id;

    private String carId;

    private String carCarId;

    private String carName;

    private String startUseTime;

    private Integer useTime;

}
