package com.cly.controller;

import com.cly.pojo.car.Car;
import com.cly.service.CarService;
import com.cly.vo.car.CarPageParams;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log/car/car")
public class CarController {

    @Autowired
    private CarService carService;

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
     * @param id
     * @return
     */
    @PutMapping("/repair/{id}")
    public Result repair(@PathVariable("id") Long id,
                         @RequestBody String description) {
        carService.repair(id, description);
        return Result.success("车辆已经进入维修状态");
    }

}
