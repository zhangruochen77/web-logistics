package com.cly.controller;

import com.cly.service.CarRepairService;
import com.cly.vo.car.CarRepairParams;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log/car/carRepair")
public class CarRepairController {

    @Autowired
    private CarRepairService carRepairService;


    /**
     * 获取车辆维修详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getRepair/{id}")
    public Result getRepair(@PathVariable("id") Long id) {
        return Result.success(carRepairService.getRepair(id));
    }

    /**
     * 删除车辆维修表信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteRepair/{id}")
    public Result deleteRepair(@PathVariable("id") Long id) {
        carRepairService.deleteRepair(id);
        return Result.success("删除信息成功");
    }

    /**
     * 分页获取车辆维修数据
     *
     * @param page
     * @param limit
     * @param params
     * @return
     */
    @PostMapping("/listRepair/{page}/{limit}")
    public Result listRepair(@PathVariable("page") Integer page,
                             @PathVariable("limit") Integer limit,
                             @RequestBody CarRepairParams params) {
        return carRepairService.listRepair(page, limit, params);
    }

    /**
     * 更新车辆状态信息
     *
     * @param id
     * @param state
     * @return
     */
    @PutMapping("/updateRepair/{id}/{state}")
    public Result updateRepair(@PathVariable("id") Long id,
                               @PathVariable("state") Integer state) {
        carRepairService.updateRepair(id, state);
        return Result.success("状态更新完成");
    }

}
