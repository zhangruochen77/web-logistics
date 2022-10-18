package com.cly.service;

import com.cly.pojo.car.CarRequire;

import java.util.List;

public interface CarRequireService {

    /**
     * 派送员申请使用车辆
     *
     * @param id      车辆的 id
     * @param adminId 派送员的 id
     * @return
     */
    Boolean requireCar(Long id, Long adminId);

    /**
     * 查看派送员其是否有申请使用车辆的请求
     *
     * @param dispatcherId
     * @return
     */
    CarRequire getCarRequire(Long dispatcherId);

    /**
     * 获取车辆申请的数量
     *
     * @return 查表 t_car_require 返回申请总数
     */
    Integer getCarRequireNumber();

    /**
     * 获取所有车辆申请操作
     *
     * @return 车辆申请操作的集合
     */
    List<CarRequire> listAll();

    /**
     * 拒绝司机申请车辆的操作
     *
     * @param id 申请车辆操作的表 id
     * @return 操作的结果
     */
    int refuseRequireById(Long id);

    /**
     * 根据司机申请表的 id 获取司机获取车辆信息
     *
     * @param id 司机申请表的 id
     * @return 司机申请车辆表的信息
     */
    CarRequire getCarRequireById(Long id);


    /**
     * 删除该申请操作信息
     *
     * @param id 申请表 id
     * @return 是否成功
     */
    int deleteById(Long id);
}
