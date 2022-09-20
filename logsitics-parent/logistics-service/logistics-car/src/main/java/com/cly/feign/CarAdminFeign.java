package com.cly.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient("logistics-admin")
public interface CarAdminFeign {


    /**
     * 解除司机和车辆的关系
     *
     * @param id
     * @return
     */
    @PutMapping("/log/admin/dispatcher/removeCarInfo/{id}")
    Boolean removeCarInfo(@PathVariable("id") Long id);

    /**
     * 获取单个用户的名字信息
     *
     * @param id
     * @return
     */
    @GetMapping("/log/admin/dispatcher/getDispatcherNameById/{id}")
    String getDispatcherNameById(@PathVariable("id") Long id);

    /**
     * 批量获取司机的姓名
     *
     * @param ids
     * @return
     */
    @PostMapping("/getDispatcherNamesByIds")
    Map<Long, String> getDispatcherNamesByIds(@RequestBody List<Long> ids);

}
