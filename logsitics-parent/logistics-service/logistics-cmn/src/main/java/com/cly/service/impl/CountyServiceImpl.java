package com.cly.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.CountyMapper;
import com.cly.pojo.cmn.County;
import com.cly.service.CountyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 区县操作实现类
 */
@Service
public class CountyServiceImpl extends ServiceImpl<CountyMapper, County> implements CountyService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过 id 获取区县
     *
     * @param id
     * @return
     */
    @Override
    public County getCountyById(Long id) {

        // redis
        String key = "log:cmn:county:" + id;
        Object redisCounty = redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(redisCounty)) {
            return JSONObject.parseObject(redisCounty.toString(), County.class);
        }

        // 数据库
        County county = baseMapper.selectById(id);
        if (!ObjectUtils.isEmpty(county)) {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(county), 1L, TimeUnit.DAYS);
        }

        return county;
    }

    /**
     * 获取具体城市下所有区县
     *
     * @param parentId
     * @return
     */
    @Override
    public List<County> getCountiesByParentId(Long parentId) {

        // redis
        String key = "log:cmn:counties:" + parentId;
        Object redisCounties = redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(redisCounties)) {
            return JSONObject.parseObject(redisCounties.toString(), List.class);
        }

        // 数据库
        List<County> counties = baseMapper.selectList(new LambdaQueryWrapper<County>().eq(County::getCityId, parentId));
        if (!ObjectUtils.isEmpty(counties)) {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(counties), 1L, TimeUnit.DAYS);
        }

        return counties;
    }

}
