package com.cly.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.ProvinceMapper;
import com.cly.pojo.cmn.Province;
import com.cly.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 省操作实现类
 */
@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province>
        implements ProvinceService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取省信息
     *
     * @param id
     * @return
     */
    @Override
    public Province getProvinceById(Long id) {

        // redis 缓存
        String key = "log:cmn:province:" + id;
        Object pro = redisTemplate.opsForValue().get(key);

        if (!ObjectUtils.isEmpty(pro)) {
            return JSONObject.parseObject(pro.toString(), Province.class);
        }

        // redis 没有 数据库查询
        Province province = baseMapper.selectById(id);

        if (!ObjectUtils.isEmpty(province)) {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(province),
                    1L, TimeUnit.DAYS);
        }

        return province;
    }

    /**
     * 获取所有省信息
     *
     * @return
     */
    @Override
    public List<Province> getAllProvinces() {

        // redis 缓存
        String key = "log:cmn:provinces";
        Object pros = redisTemplate.opsForValue().get(key);

        if (!ObjectUtils.isEmpty(pros)) {
            return JSONObject.parseObject(pros.toString(), List.class);
        }

        List<Province> provinces = baseMapper.selectList(null);

        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(provinces), 1L, TimeUnit.DAYS);

        return provinces;
    }
}
