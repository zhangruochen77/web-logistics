package com.cly.service;

import java.util.List;
import java.util.Map;

public interface DispatcherService {

    /**
     * 解除司机和车辆的关系
     *
     * @param id
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

}
