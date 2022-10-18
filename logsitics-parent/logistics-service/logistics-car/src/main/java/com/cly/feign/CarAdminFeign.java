package com.cly.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient("logistics-admin")
public interface CarAdminFeign {


    /**
     * 解除司机和车辆的关系
     *
     * @param id 主键 id
     * @return 是否将表 t_dispatcher 中车辆信息移除成功
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
    @PostMapping("/log/admin/dispatcher/getDispatcherNamesByIds")
    Map<Long, String> getDispatcherNamesByAdminIds(@RequestBody List<Long> ids);


    /**
     * 添加车辆和司机的关系
     *
     * @param carId
     * @param id
     * @return
     */
    @PutMapping("/log/admin/dispatcher/relateCarAndDispatcher/{carId}/{id}")
    Boolean relateCarAndDispatcher(@PathVariable("carId") Long carId,
                                   @PathVariable("id") Long id);

    /**
     * 获取司机和车辆表关联的id信息
     *
     * @param id
     * @return
     */
    @GetMapping("/log/admin/dispatcher/getDispatcherId/{id}")
    Long getDispatcherId(@PathVariable("id") Long id);

    /**
     * 一次性解除司机和车辆的多条关系
     *
     * @param ids
     * @return 成功的记录数
     */
    @DeleteMapping("/log/admin/dispatcher/deleteDispatcherByIds")
    Integer deleteDispatcherByAdminIds(@RequestBody Set<Long> ids);

}
