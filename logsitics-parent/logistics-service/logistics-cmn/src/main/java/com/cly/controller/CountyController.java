package com.cly.controller;

import com.cly.service.CountyService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典 区县 接口
 */
@RestController                 
@RequestMapping("/log/cmn/county")
public class CountyController {

    @Autowired
    private CountyService countyService;

    /**
     * 通过 id 获取区县
     *
     * @param id
     * @return
     */
    @GetMapping("getCountyById/{id}")
    public Result getCountyById(@PathVariable("id") Long id) {
        return Result.success(countyService.getCountyById(id));
    }

    /**
     * 获取具体城市下所有区县
     *
     * @param parentId
     * @return
     */
    @GetMapping("getCountiesByParentId/{parentId}")
    public Result getCountiesByParentId(@PathVariable("parentId") Long parentId) {
        return Result.success(countyService.getCountiesByParentId(parentId));
    }

}
