package com.cly.service;

import com.cly.pojo.cmn.County;

import java.util.List;

public interface CountyService {

    /**
     * 通过 id 获取区县
     *
     * @param id
     * @return
     */
    County getCountyById(Long id);

    /**
     * 获取具体城市下所有区县
     *
     * @param parentId
     * @return
     */
    List<County> getCountiesByParentId(Long parentId);

}
