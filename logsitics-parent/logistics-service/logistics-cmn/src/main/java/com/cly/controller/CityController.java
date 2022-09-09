package com.cly.controller;

import com.cly.service.CityService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典 城市 接口
 */
@RestController
@RequestMapping("/log/cmn/city")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * 根据 id 获取城市信息
     *
     * @param id
     * @return
     */
    @GetMapping("getCityById/{id}")
    public Result getCityById(@PathVariable("id") Long id) {
        return Result.success(cityService.getCityById(id));
    }

    /**
     * 获取某个省的所有城市通过城市值
     *
     * @param parentId
     * @return
     */
    @GetMapping("getAllCitiesByParentId/{parentId}")
    public Result getAllCitiesByParentId(@PathVariable("parentId") Long parentId) {
        return Result.success(cityService.getAllCitiesByParentId(parentId));
    }

}
