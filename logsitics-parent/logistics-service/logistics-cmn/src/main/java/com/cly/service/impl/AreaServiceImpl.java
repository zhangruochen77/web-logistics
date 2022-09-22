package com.cly.service.impl;

import com.cly.service.AreaService;
import com.cly.service.CityService;
import com.cly.service.CountyService;
import com.cly.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CountyService countyService;


    /**
     * 获取省市区地址
     *
     * @param province
     * @param city
     * @param county
     * @return
     */
    @Override
    public String getArea(Long province, Long city, Long county) {
        String proValue = provinceService.getProvinceById(province).getValue();
        String cityValue = cityService.getCityById(city).getValue();
        String countyValue = countyService.getCountyById(county).getValue();

        StringBuilder builder = new StringBuilder(proValue.length() + cityValue.length() + countyValue.length());

        builder.append(proValue);
        builder.append(cityValue);
        builder.append(countyValue);

        return builder.toString();
    }
}
