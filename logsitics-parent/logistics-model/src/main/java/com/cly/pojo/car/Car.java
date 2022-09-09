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
    private Long carId;

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
    private Date useTime;

}
