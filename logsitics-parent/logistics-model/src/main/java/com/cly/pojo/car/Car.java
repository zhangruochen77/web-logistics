package com.cly.pojo.car;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car extends Base {

    /**
     * 车牌号码
     */
    private String carId;

    /**
     * 配送员 id
     */
    private Long dispatcherId;

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 开始使用时间
     */
    private Date startUseTime;

    /**
     * 车辆状态 1 表示使用 0 表示处于维修状态当中
     */
    private Integer state;

}
