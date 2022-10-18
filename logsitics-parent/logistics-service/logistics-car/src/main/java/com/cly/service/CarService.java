package com.cly.service;

import com.cly.pojo.car.Car;
import com.cly.vo.car.*;

import java.util.List;
import java.util.Map;

public interface CarService {

    /**
     * 添加新车辆
     *
     * @param car
     * @return
     */
    void saveCar(Car car);

    /**
     * 删除车辆
     *
     * @param id
     */
    void deleteCarById(Long id);

    /**
     * 更新车辆信息
     *
     * @param car
     */
    void updateCar(Car car);

    /**
     * 获取车辆信息
     *
     * @param id
     * @return
     */
    CarVo getCarById(Long id);

    /**
     * 分页条件查询
     *
     * @param page
     * @param limit
     * @param params
     * @return
     */
    Map<String, Object> pageFind(Integer page, Integer limit, CarPageParams params);

    /**
     * 车辆维修操作
     *
     * @param id
     * @param description
     */
    void repair(Long id, String description);

    /**
     * 获取车辆信息
     *
     * @param id
     * @return
     */
    Car getCar(Long id);

    /**
     * 批量获取车辆信息
     *
     * @param carIds
     * @return
     */
    Map<Long, Car> listCarByIds(List<Long> carIds);

    /**
     * 解除司机和车辆的关系
     *
     * @param id
     */
    void dissolveRelationship(Long id);

    /**
     * 添加司机和车辆的关系
     *
     * @param carId
     * @param dispatcherId
     */
    void relateCarAndDispatcher(Long carId, Long dispatcherId);

    /**
     * 获取派送员车辆信息
     *
     * @return
     */
    CarDispatcherVo getCar();

    /**
     * 司机分页查找未分配可以使用的车辆信息
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> pageFindNotDispatchCar(Integer page, Integer limit);

    /**
     * 派送员申请使用车辆
     *
     * @param id
     * @return
     */
    Boolean requireCar(Long id);

    /**
     * 获取派送员是否申请了车辆
     *
     * @return
     */
    CarRequireVo getCarRequire();

    /**
     * 一次性移除多条车辆信息
     *
     * @param carIds 车辆的 id
     * @return
     */
    void listDeleteCars(Long[] carIds);

    /**
     * 获取车辆申请的数量
     *
     * @return 查表 t_car_require 返回申请总数
     */
    Integer getCarRequireNumber();

    /**
     * 查询全部的车辆申请
     *
     * @return 司机和其申请车辆的信息
     */
    List<CarRequirePageVo> getCarRequires();

    /**
     * 同意司机的车辆申请操作
     *
     * @param id 申请车辆操作的表的 id
     * @return 操作的结果
     */
    boolean succeedRequireById(Long id);
}
