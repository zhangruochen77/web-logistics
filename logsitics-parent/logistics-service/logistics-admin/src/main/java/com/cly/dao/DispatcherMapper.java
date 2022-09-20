package com.cly.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cly.pojo.admin.Dispatcher;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatcherMapper extends BaseMapper<Dispatcher> {

    /**
     * 移除司机和车辆信息关系
     *
     * @param id
     * @return
     */
    int removeCarInfo(Long id);
}
