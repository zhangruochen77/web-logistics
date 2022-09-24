package com.cly.vo.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPageParams {

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 范围开始时间
     */
    private Date startTime;

    /**
     * 范围结束时间
     */
    private Date endTime;

    /**
     * 是否分配
     */
    private Boolean isDispatch;

}
