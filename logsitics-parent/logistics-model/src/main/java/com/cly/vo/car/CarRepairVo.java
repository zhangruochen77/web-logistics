package com.cly.vo.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRepairVo {

    private String id;

    /**
     * 维修车辆的 id 值
     */
    private String carId;

    /**
     * 车牌号
     */
    private String carCarId;

    /**
     * 维修车辆的名称
     */
    private String carName;

    /**
     * 开始维修时间
     */
    private String startTime;

    /**
     * 结束维修时间
     */
    private String endTime;

    /**
     * 车辆维修描述
     */
    private String description;

    /**
     * 车辆维修状态  0 表示未开始 1 正在维修 2 表明维修结束
     */
    private Integer state;
}
