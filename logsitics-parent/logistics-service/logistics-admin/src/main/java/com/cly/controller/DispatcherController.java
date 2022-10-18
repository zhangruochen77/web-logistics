package com.cly.controller;

import com.cly.service.DispatcherService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/log/admin/dispatcher")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;


    /**
     * 解除司机和车辆的关系
     *
     * @param id 主键 id
     * @return
     */
    @PutMapping("/removeCarInfo/{id}")
    public Boolean removeCarInfo(@PathVariable("id") Long id) {
        return dispatcherService.removeCarInfo(id);
    }

    /**
     * 获取单个司机的名字信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getDispatcherNameById/{id}")
    public String getDispatcherNameById(@PathVariable("id") Long id) {
        return dispatcherService.getDispatcherNameById(id);
    }


    /**
     * 批量获取司机的姓名通过 ids
     *
     * @param ids
     * @return
     */
    @PostMapping("/getDispatcherNamesByIds")
    public Map<Long, String> getDispatcherNamesByAdminIds(@RequestBody List<Long> ids) {
        return dispatcherService.getDispatcherNamesByIds(ids);
    }

    /**
     * 添加车辆和司机的关系
     *
     * @param carId
     * @param id
     * @return
     */
    @PutMapping("/relateCarAndDispatcher/{carId}/{id}")
    public Boolean relateCarAndDispatcher(@PathVariable("carId") Long carId,
                                          @PathVariable("id") Long id) {
        return dispatcherService.relateCarAndDispatcher(carId, id);
    }

    /**
     * 获取司机及其 id 信息
     *
     * @return
     */
    @GetMapping("/listDispatcher")
    public Result listDispatcher() {
        return Result.success(dispatcherService.listDispatcher());
    }

    /**
     * 获取司机和车辆表关联的id信息
     *
     * @param adminId
     * @return
     */
    @GetMapping("/getDispatcherId/{adminId}")
    public Long getDispatcherId(@PathVariable("adminId") Long adminId) {
        return dispatcherService.getDispatcherId(adminId);
    }


    /**
     * 一次性解除司机和车辆的多条关系
     *
     * @param ids 司机的主键 id
     * @return 成功的记录数
     */
    @DeleteMapping("/deleteDispatcherByIds")
    public Integer deleteDispatcherByAdminIds(@RequestBody Set<Long> ids) {
        return dispatcherService.deleteDispatcherByAdminIds(ids);
    }

}
