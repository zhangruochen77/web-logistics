package com.cly.service;

import com.cly.vo.car.CarRepairParams;
import com.cly.vo.car.CarRepairVo;
import com.cly.web.Result;

public interface CarRepairService {

    /**
     * 生成维修信息
     *
     * @param id
     * @param description
     * @return
     */
    boolean repair(Long id, String description);


    /**
     * 获取维修车辆详细信息
     *
     * @param id
     * @return
     */
    CarRepairVo getRepair(Long id);

    /**
     * 删除车辆维修表信息
     *
     * @param id
     */
    void deleteRepair(Long id);

    /**
     * 分页获取车辆维修数据
     *
     * @param page
     * @param limit
     * @param params
     * @return
     */
    Result listRepair(Integer page, Integer limit, CarRepairParams params);

    /**
     * 更新车辆维修状态信息
     *
     * @param id
     * @param state
     */
    void updateRepair(Long id, Integer state);

}
