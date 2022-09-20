package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.common.DateFormatUtils;
import com.cly.dao.CarRepairMapper;
import com.cly.exception.LogException;
import com.cly.pojo.car.Car;
import com.cly.pojo.car.CarRepair;
import com.cly.service.CarRepairService;
import com.cly.service.CarService;
import com.cly.vo.car.CarRepairParams;
import com.cly.vo.car.CarRepairVo;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class CarRepairServiceImpl extends ServiceImpl<CarRepairMapper, CarRepair>
        implements CarRepairService {


    @Autowired
    private CarService carService;

    /**
     * 生成维修信息
     *
     * @param id
     * @param description
     * @return
     */
    @Override
    public boolean repair(Long id, String description) {
        CarRepair carRepair = createCarRepair(id, description);
        return baseMapper.insert(carRepair) == 1;
    }

    /**
     * 获取维修车辆详细信息
     *
     * @param id
     * @return
     */
    @Override
    public CarRepairVo getRepair(Long id) {
        CarRepair carRepair = baseMapper.selectById(id);
        Car car = carService.getCar(id);
        return carRepairToCarRepairVo(carRepair, car.getCarId(), car.getName());
    }

    /**
     * 删除车辆维修表信息
     *
     * @param id
     */
    @Override
    public void deleteRepair(Long id) {
        baseMapper.deleteById(id);
    }

    /**
     * 分页获取车辆维修数据
     *
     * @param page
     * @param limit
     * @param params
     * @return
     */
    @Override
    public Result listRepair(Integer page, Integer limit, CarRepairParams params) {
        LambdaQueryWrapper<CarRepair> wrapper = createQueryWrapper(params);
        IPage<CarRepair> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, wrapper);

        // 构造数据
        List<CarRepair> repairs = iPage.getRecords();
        List<Long> carIds = new ArrayList<>(repairs.size());
        Map<String, Object> result = new HashMap<>(4);
        List<CarRepairVo> vos = new ArrayList<>();
        result.put("total", iPage.getTotal());
        result.put("data", vos);

        repairs.forEach(item -> {
            carIds.add(item.getCarId());
        });

        // 填充车辆信息
        Map<Long, Car> cars = carService.listCarByIds(carIds);
        repairs.forEach(item -> {
            Car car = cars.get(item.getCarId());
            vos.add(carRepairToCarRepairVo(item, car.getCarId(), car.getName()));
        });

        return Result.success(result);
    }

    /**
     * 更新车辆维修状态信息
     * 判断维修状态是否正确 正确情况下应该正向进行
     * 未开始（0） -> 维修中（1） -> 维修完成（2）
     * 维修完成之后需要更新车辆的状态 使得车辆能重新投入使用
     *
     * @param id
     * @param state
     */
    @Override
    public void updateRepair(Long id, Integer state) {
        CarRepair carRepair = baseMapper.selectById(id);

        if (state.equals(carRepair.getState())) {
            throw new LogException("维修状态修改不正确,没有进行修改");
        }
        if (carRepair.getState().compareTo(state) == -1) {
            throw new LogException("维修状态修改不正确,不能逆向操作");
        }

        // 维修完成状态
        if (state.equals(2)) {
            carRepair.setEndTime(new Date());

            // 更新车辆信息
            Car car = new Car();
            car.setId(carRepair.getId());
            car.setState(1);
            carService.updateCar(car);
        }

        carRepair.setState(state);
        baseMapper.updateById(carRepair);
    }

    /**
     * 构造分页条件
     *
     * @param params
     * @return
     */
    private LambdaQueryWrapper<CarRepair> createQueryWrapper(CarRepairParams params) {
        LambdaQueryWrapper<CarRepair> wrapper = new LambdaQueryWrapper<>();

        if (!ObjectUtils.isEmpty(params)) {
            if (!ObjectUtils.isEmpty(params.getStartTime())) {
                wrapper.lt(CarRepair::getStartTime, params.getStartTime());
            }
            if (!ObjectUtils.isEmpty(params.getEndTime())) {
                wrapper.gt(CarRepair::getEndTime, params.getEndTime());
            }
            if (!ObjectUtils.isEmpty(params.getState())) {
                wrapper.eq(CarRepair::getState, params.getState());
            }
        }

        return wrapper;
    }

    /**
     * 转化为 carRepairVo 对象
     *
     * @param repair
     * @param carCarId
     * @param name
     * @return
     */
    private CarRepairVo carRepairToCarRepairVo(CarRepair repair, String carCarId, String name) {
        CarRepairVo vo = new CarRepairVo();
        vo.setCarName(name);
        vo.setCarCarId(carCarId);
        vo.setState(repair.getState());
        vo.setId(repair.getId().toString());
        vo.setCarId(repair.getCarId().toString());
        vo.setDescription(repair.getDescription());
        vo.setEndTime(DateFormatUtils.dateFormat(repair.getEndTime()));
        vo.setStartTime(DateFormatUtils.dateFormat(repair.getStartTime()));
        return vo;
    }

    /**
     * 创建维修对象
     *
     * @param id
     * @param description
     * @return
     */
    private CarRepair createCarRepair(Long id, String description) {
        CarRepair carRepair = new CarRepair();
        carRepair.setState(0);
        carRepair.setCarId(id);
        carRepair.setStartTime(new Date());
        carRepair.setDescription(description);
        return carRepair;
    }

}
