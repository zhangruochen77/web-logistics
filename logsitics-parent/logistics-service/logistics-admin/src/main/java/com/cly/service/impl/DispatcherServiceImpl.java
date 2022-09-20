package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.DispatcherMapper;
import com.cly.pojo.admin.Dispatcher;
import com.cly.service.DispatcherService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DispatcherServiceImpl extends ServiceImpl<DispatcherMapper, Dispatcher>
        implements DispatcherService {

    /**
     * 解除司机和车辆的关系
     *
     * @param id
     * @return
     */
    @Override
    public Boolean removeCarInfo(Long id) {
        return baseMapper.update(null, new LambdaUpdateWrapper<Dispatcher>().set(Dispatcher::getCarId, null).eq(Dispatcher::getId, id)) == 1;
    }

    /**
     * 获取单个用户的名字信息
     *
     * @param id
     * @return
     */
    @Override
    public String getDispatcherNameById(Long id) {
        return baseMapper.selectById(id).getName();
    }

    /**
     * 批量获取用户的姓名
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, String> getDispatcherNamesByIds(List<Long> ids) {
        List<Dispatcher> dispatchers = baseMapper.selectBatchIds(ids);
        Map<Long, String> result = new HashMap<>(dispatchers.size() << 1);
        dispatchers.forEach(item -> result.put(item.getId(), item.getName()));
        return result;
    }

}
