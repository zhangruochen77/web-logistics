package com.cly.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 司机类 主要起员工和车辆之间的连接作用
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dispatcher {

    private Long id;

    private Long adminId;

    private String name;

    private Long carId;
}
