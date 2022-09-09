package com.cly.service;

import com.cly.pojo.cmn.Province;

import java.util.List;

public interface ProvinceService {

    /**
     * 获取省信息
     *
     * @param id
     * @return
     */
    Province getProvinceById(Long id);

    /**
     * 获取所有省信息
     *
     * @return
     */
    List<Province> getAllProvinces();
}
