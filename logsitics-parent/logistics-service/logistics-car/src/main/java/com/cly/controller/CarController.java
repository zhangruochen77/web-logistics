package com.cly.controller;

import com.cly.pojo.car.Car;
import com.cly.service.CarRequireService;
import com.cly.service.CarService;
import com.cly.vo.car.CarPageParams;
import com.cly.vo.car.CarRepairDispatcherParams;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log/car/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRequireService carRequireService;

    /**
     * 添加新车辆
     *
     * @param car
     * @return
     */
    @PostMapping("/saveCar")
    public Result saveCar(@RequestBody Car car) {
        carService.saveCar(car);
        return Result.success("车辆添加成功");
    }


    /**
     * 移除车辆
     *
     * @param id
     * @return
     */
    @DeleteMapping("/removeCarById/{id}")
    public Result deleteCarById(@PathVariable("id") Long id) {
        carService.deleteCarById(id);
        return Result.success("车辆移除成功");
    }


    /**
     * 更新车辆的信息
     *
     * @param car
     * @return
     */
    @PutMapping("/updateCar")
    public Result updateCar(@RequestBody Car car) {
        carService.updateCar(car);
        return Result.success("车辆信息更新成功！");
    }

    /**
     * 获取车辆信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getCarById/{id}")
    public Result getCarById(@PathVariable("id") Long id) {
        return Result.success(carService.getCarById(id));
    }

    /**
     * 分页条件查询
     *
     * @param page
     * @param limit
     * @param params
     * @return
     */
    @PostMapping("/pageFind/{page}/{limit}")
    public Result pageFind(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit,
                           @RequestBody CarPageParams params) {
        return Result.success(carService.pageFind(page, limit, params));
    }

    /**
     * 车辆维修
     *
     * @param params
     * @return
     */
    @PutMapping("/repair")
    public Result repair(@RequestBody CarRepairDispatcherParams params) {
        carService.repair(params.getId(), params.getDescription());
        return Result.success("车辆已经进入维修状态");
    }


    /**
     * 解除司机和车辆的关系
     *
     * @param id
     * @return
     */
    @PutMapping("/dissolveRelationship/{id}")
    public Result dissolveRelationship(@PathVariable Long id) {
        carService.dissolveRelationship(id);
        return Result.success("解除关系成功");
    }

    /**
     * 添加司机和车辆的关系
     *
     * @param carId
     * @param dispatcherId
     * @return
     */
    @PutMapping("/relateCarAndDispatcher/{carId}/{dispatcherId}")
    public Result relateCarAndDispatcher(@PathVariable("carId") Long carId,
                                         @PathVariable("dispatcherId") Long dispatcherId) {
        carService.relateCarAndDispatcher(carId, dispatcherId);
        return Result.success("添加关系成功");
    }

    /**
     * 获取派送员车辆信息
     *
     * @return
     */
    @GetMapping("/getCar")
    public Result getCar() {
        return Result.success(carService.getCar());
    }

    /**
     * 司机分页查找未分配可以使用的车辆信息
     *
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/pageFindNotDispatchCar/{page}/{limit}")
    public Result pageFindNotDispatchCar(@PathVariable("page") Integer page,
                                         @PathVariable("limit") Integer limit) {
        return Result.success(carService.pageFindNotDispatchCar(page, limit));
    }

    /**
     * 派送员申请使用车辆
     *
     * @param id 请求车辆的id
     * @return
     */
    @PostMapping("/require/{id}")
    public Result requireCar(@PathVariable("id") Long id) {
        return Result.success(carService.requireCar(id));
    }


    /**
     * 获取派送员是否申请了车辆
     *
     * @return
     */
    @GetMapping("/getCarRequire")
    public Result getCarRequire() {
        return Result.success(carService.getCarRequire());
    }

    /**
     * 一次性移除多条车辆信息
     *
     * @param carIds 车辆的 id
     * @return
     */
    @DeleteMapping("/listDeleteCars")
    public Result listDeleteCars(@RequestBody Long[] carIds) {
        carService.listDeleteCars(carIds);
        return Result.success();
    }

    /**
     * 获取车辆申请的数量
     *
     * @return 查表 t_car_require 返回申请总数
     */
    @GetMapping("/getCarRequireNumber")
    public Result getCarRequireNumber() {
        return Result.success(carService.getCarRequireNumber());
    }

    /**
     * 查询全部的车辆申请
     *
     * @return 司机和其申请车辆的信息
     */
    @GetMapping("/getCarRequires")
    public Result getCarsRequires() {
        return Result.success(carService.getCarRequires());
    }


    /**
     * 拒绝司机申请车辆的操作
     *
     * @param id 申请车辆操作的表 id
     * @return 操作的结果
     */
    @DeleteMapping("/require/{id}")
    public Result refuseRequireById(@PathVariable Long id) {
        int res = carRequireService.refuseRequireById(id);
        return res == 1 ? Result.success("操作成功!") : Result.fail("操作失败！");
    }

    /**
     * 同意司机的车辆申请操作
     *
     * @param id 申请车辆操作的表的 id
     * @return 操作的结果
     */
    @PutMapping("/require/{id}")
    public Result succeedRequireById(@PathVariable Long id) {
        boolean res = carService.succeedRequireById(id);
        return res ? Result.success("操作成功!") : Result.fail("操作失败！");
    }

}
