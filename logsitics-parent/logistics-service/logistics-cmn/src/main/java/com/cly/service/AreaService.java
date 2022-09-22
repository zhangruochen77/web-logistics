package com.cly.service;

public interface AreaService {

    /**
     * 获取省市区地址
     *
     * @param province
     * @param city
     * @param county
     * @return
     */
    String getArea(Long province, Long city, Long county);
}
