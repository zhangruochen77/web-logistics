package com.cly.vo.car;

import lombok.Data;

import java.util.Date;

@Data
public class CarRepairParams {

    /**
     * 开始维修时间
     */
    private Date startTime;

    /**
     * 结束维修时间
     */
    private Date endTime;

    /**
     * 车辆维修状态  0 表示未开始 1 并且没有结束时间正在维修 1 并且有结束时间表明维修结束
     */
    private Integer state;

}
