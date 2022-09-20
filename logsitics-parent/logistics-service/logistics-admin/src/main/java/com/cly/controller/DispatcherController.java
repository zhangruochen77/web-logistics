package com.cly.controller;

import com.cly.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log/admin/dispatcher")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;


    /**
     * 解除司机和车辆的关系
     *
     * @param id
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
     * 批量获取司机的姓名
     *
     * @param ids
     * @return
     */
    @PostMapping("/getDispatcherNamesByIds")
    public Map<Long, String> getDispatcherNamesByIds(@RequestBody List<Long> ids) {
        return dispatcherService.getDispatcherNamesByIds(ids);
    }

}
