package com.cly.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("logistics-cmn")
public interface OrderCmnFeign {


    /**
     * 直接获取到省市区地址
     *
     * @return
     */
    @GetMapping("/log/cmn/area/getArea/{province}/{city}/{county}")
    String getArea(@PathVariable("province") Long province,
                          @PathVariable("city") Long city,
                          @PathVariable("county") Long county);

}
