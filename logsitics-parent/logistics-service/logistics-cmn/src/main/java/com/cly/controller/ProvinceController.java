package com.cly.controller;

import com.cly.service.ProvinceService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典 省份 接口
 */
@RestController
@RequestMapping("/log/cmn/province")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    /**
     * 获取省信息
     *
     * @param id
     * @return
     */
    @GetMapping("getProvinceById/{id}")
    public Result getProvinceById(@PathVariable("id") Long id) {
        return Result.success(provinceService.getProvinceById(id));
    }

    /**
     * 获取所有省信息
     *
     * @return
     */
    @GetMapping("getAllProvinces")
    public Result getAllProvinces() {
        return Result.success(provinceService.getAllProvinces());
    }

}
