package com.cly.pojo.car;

import lombok.Data;

/**
 * 请求使用车辆实体类
 */
@Data
public class CarRequire {

    private Long id;

    private Long adminId;

    private Long carId;
}
