package com.cly.service;

import com.cly.pojo.car.Car;
import com.cly.vo.car.CarPageParams;
import com.cly.vo.car.CarVo;

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
}
