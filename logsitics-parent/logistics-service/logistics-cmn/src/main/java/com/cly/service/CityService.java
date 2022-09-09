package com.cly.service;

import com.cly.pojo.cmn.City;

import java.util.List;

public interface CityService {

    /**
     * 根据 城市 id 获取城市信息
     *
     * @param id
     * @return
     */
    City getCityById(Long id);

    /**
     * 获取某个省的所有城市通过城市值
     *
     * @param parentId
     * @return
     */
    List<City> getAllCitiesByParentId(Long parentId);
}
