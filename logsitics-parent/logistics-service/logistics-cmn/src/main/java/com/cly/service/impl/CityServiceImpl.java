package com.cly.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.CityMapper;
import com.cly.pojo.cmn.City;
import com.cly.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 城市数据字典服务实现类
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据 城市 id 获取城市信息
     *
     * @param id
     * @return
     */
    @Override
    public City getCityById(Long id) {

        // redis 查询
        String key = "log:cmn:city:" + id;
        Object c = redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(c)) {
            return JSONObject.parseObject(c.toString(), City.class);
        }

        // 数据库查询
        City city = baseMapper.selectById(id);
        if (!ObjectUtils.isEmpty(city)) {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(city), 1L, TimeUnit.DAYS);
        }

        return city;
    }

    /**
     * 获取某个省的所有城市通过城市值
     *
     * @param parentId
     * @return
     */
    @Override
    public List<City> getAllCitiesByParentId(Long parentId) {

        // redis 查询
        String key = "log:cmn:cities";
        Object cs = redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(cs)) {
            return JSONObject.parseObject(cs.toString(), List.class);
        }

        // 数据库查询
        List<City> cities = baseMapper.selectList(new LambdaQueryWrapper<City>().eq(City::getProvinceId, parentId));
        if (!ObjectUtils.isEmpty(cities)) {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(cities), 1L, TimeUnit.DAYS);
        }

        return cities;
    }
}
