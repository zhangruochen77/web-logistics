package com.cly.feign;

import com.cly.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("logistics-cmn")
public interface WarehouseCmnFeign {

    /**
     * 获取省信息
     *
     * @param id
     * @return
     */
    @GetMapping("/log/cmn/province/getProvinceById/{id}")
    Result getProvinceById(@PathVariable("id") Long id);

    /**
     * 获取城市信息
     *
     * @param id
     * @return
     */
    @GetMapping("/log/cmn/city/getCityById/{id}")
    Result getCityById(@PathVariable("id") Long id);

    /**
     * 获取区县信息
     *
     * @param id
     * @return
     */
    @GetMapping("/log/cmn/county/getCountyById/{id}")
    Result getCountyById(@PathVariable("id") Long id);

}
