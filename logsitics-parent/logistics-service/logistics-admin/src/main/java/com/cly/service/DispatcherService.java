package com.cly.service;

import com.cly.vo.admin.DispatcherVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DispatcherService {

    /**
     * 解除司机和车辆的关系
     *
     * @param id  主键 id
     * @return
     */
    Boolean removeCarInfo(Long id);

    /**
     * 获取单个用户的名字信息
     *
     * @param id
     * @return
     */
    String getDispatcherNameById(Long id);

    /**
     * 批量获取用户的姓名
     *
     * @param ids
     * @return
     */
    Map<Long, String> getDispatcherNamesByIds(List<Long> ids);

    /**
     * 添加车辆和司机的关系
     *
     * @param carId
     * @param id
     * @return
     */
    Boolean relateCarAndDispatcher(Long carId, Long id);

    /**
     * 获取司机及其 id 信息
     *
     * @return
     */
    List<DispatcherVo> listDispatcher();

    /**
     * 获取司机和车辆表关联的id信息
     *
     * @param adminId
     * @return
     */
    Long getDispatcherId(Long adminId);

    /**
     * 一次性解除司机和车辆的多条关系
     *
     * @param ids 司机的主键 id
     * @return 成功的记录数
     */
    Integer deleteDispatcherByAdminIds(Set<Long> ids);

}
