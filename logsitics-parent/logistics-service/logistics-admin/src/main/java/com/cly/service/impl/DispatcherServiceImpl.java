package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.DispatcherMapper;
import com.cly.pojo.admin.Dispatcher;
import com.cly.service.DispatcherService;
import com.cly.vo.admin.DispatcherVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DispatcherServiceImpl extends ServiceImpl<DispatcherMapper, Dispatcher>
        implements DispatcherService {

    /**
     * 解除司机和车辆的关系
     *
     * @param id 主键 id
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

    /**
     * 添加车辆和司机的关系
     *
     * @param carId
     * @param id
     * @return
     */
    @Override
    public Boolean relateCarAndDispatcher(Long carId, Long id) {
        Dispatcher dispatcher = baseMapper.selectById(id);
        if (!ObjectUtils.isEmpty(dispatcher.getCarId())) {
            return false;
        }

        return baseMapper.update(null, new LambdaUpdateWrapper<Dispatcher>().set(Dispatcher::getCarId, carId).eq(Dispatcher::getId, id)) == 1;
    }

    /**
     * 获取司机及其 id 信息  需要查找没有车辆信息
     *
     * @return
     */
    @Override
    public List<DispatcherVo> listDispatcher() {
        List<Dispatcher> dispatchers = baseMapper.selectList(new LambdaQueryWrapper<Dispatcher>().isNull(Dispatcher::getCarId));
        return dispatchers.stream().map(dispatcher -> {
            DispatcherVo vo = new DispatcherVo();
            vo.setId(dispatcher.getId().toString());
            vo.setName(dispatcher.getName());

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取司机和车辆表关联的id信息
     *
     * @param id
     * @return
     */
    @Override
    public Long getDispatcherId(Long id) {
        Dispatcher dispatcher = baseMapper.selectOne(new LambdaQueryWrapper<Dispatcher>().eq(Dispatcher::getId, id));
        return dispatcher.getId();
    }

    /**
     * 一次性解除司机和车辆的多条关系
     *
     * @param ids 司机的主键 id
     * @return 成功的记录数
     */
    @Override
    public Integer deleteDispatcherByAdminIds(Set<Long> ids) {
        return baseMapper.update(null, new LambdaUpdateWrapper<Dispatcher>().set(Dispatcher::getCarId, null).in(Dispatcher::getId, ids));
    }

}
