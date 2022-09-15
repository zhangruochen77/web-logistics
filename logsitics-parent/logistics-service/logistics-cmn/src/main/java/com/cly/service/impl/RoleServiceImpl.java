package com.cly.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.RoleMapper;
import com.cly.pojo.cmn.Role;
import com.cly.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 角色操作实现类
 * 该实现类所操作的角色在项目启动首次会在 将 数据查出存放入 redis 数据库当中
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过角色 id 获取角色
     */
    @Override
    public Role getRoleById(Long id) {

        // redis 缓存查询
        String key = "log:cmn:role:" + id;
        Object r = redisTemplate.opsForValue().get(key);

        // 获取到数据 则转化为 Role 对象返回
        if (!ObjectUtils.isEmpty(r)) {
            return JSONObject.parseObject(r.toString(), Role.class);
        }

        // mysql 查询 处理数据放入 redis
        Role role = baseMapper.selectById(id);
        if (!ObjectUtils.isEmpty(role)) {
            String value = JSONObject.toJSONString(role);
            redisTemplate.opsForValue().set(key, value);
            return role;
        }

        return null;
    }

    /**
     * 获取所有的角色集合
     *
     * @return
     */
    @Override
    public List<Role> getAllRole() {

        // redis 查询
        String key = "log:cmn:roles";
        Object res = redisTemplate.opsForValue().get(key);

        if (!ObjectUtils.isEmpty(res)) {
            return JSONObject.parseObject(res.toString(), List.class);
        }

        // 数据库查询
        List<Role> roles = baseMapper.selectList(null);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(roles), 1L, TimeUnit.DAYS);

        return roles;
    }

    /**
     * 更新角色信息
     *
     * @param role
     */
    @Override
    public void updateRole(Role role) {
        baseMapper.updateById(role);
    }
}
