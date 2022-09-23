package com.cly.controller;

import com.cly.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/log/cmn/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    /**
     * 直接获取到省市区地址
     *
     * @return
     */
    @GetMapping("/getArea/{province}/{city}/{county}")
    public String getArea(@PathVariable("province") Long province,
                          @PathVariable("city") Long city,
                          @PathVariable("county") Long county) {
        return areaService.getArea(province, city, county);
    }

}
