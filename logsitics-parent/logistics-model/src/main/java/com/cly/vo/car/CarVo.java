package com.cly.vo.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarVo {

    /**
     * 主键
     */
    private String id;

    /**
     * 车牌号码
     */
    private String carId;

    /**
     * 配送员 id
     */
    private String dispatcherId;

    /**
     * 配送员名称
     */
    private String dispatcherName;

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

    /**
     * 车辆状态
     */
    private Integer state;

}
