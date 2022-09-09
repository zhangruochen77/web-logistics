package com.cly.pojo.car;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRepair extends Base {

    /**
     * 维修车辆的 id 值
     */
    private Long carId;

    /**
     * 开始维修时间
     */
    private Date startTime;

    /**
     * 结束维修时间
     */
    private Date endTime;

    /**
     * 车辆维修描述
     */
    private String description;

    /**
     * 车辆维修状态
     */
    private Integer state;

}
