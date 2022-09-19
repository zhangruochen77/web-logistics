package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.common.DateFormatUtils;
import com.cly.common.TimeUtils;
import com.cly.dao.CarMapper;
import com.cly.exception.LogException;
import com.cly.pojo.car.Car;
import com.cly.service.CarService;
import com.cly.vo.car.CarPageParams;
import com.cly.vo.car.CarVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {


    /**
     * 添加新车辆
     * 确认牌照是否重复 车牌照也是唯一的
     *
     * @param car
     * @return
     */
    @Override
    public void saveCar(Car car) {
        Car c = baseMapper.selectOne(new LambdaQueryWrapper<Car>().eq(Car::getCarId, car.getCarId()));
        if (!ObjectUtils.isEmpty(c)) {
            throw new LogException("车辆牌照已经被占用，请确认！");
        }

        car.setStartUseTime(new Date());
        car.setState(1);
        baseMapper.insert(car);

    }

    /**
     * 删除车辆
     *
     * @param id
     */
    @Override
    public void deleteCarById(Long id) {
        // TODO: 2022/9/19 移除相关的订单信息
        baseMapper.deleteById(id);
    }

    /**
     *         LambdaQueryWrapper<Car> wrapper = new LambdaQueryWrapper<>();
     *         if (!ObjectUtils.isEmpty(car.getDispatcherId())) {
     *             wrapper.eq(Car::getDispatcherId, car.getDispatcherId());
     *         }
     *         if (!StringUtils.isEmpty(car.getCarId())) {
     *             wrapper.eq(Car::getCarId, car.getCarId());
     *         }
     *         if (!StringUtils.isEmpty(car.getName())) {
     *             wrapper.eq(Car::getName, car.getName());
     *         }
     */
    /**
     * 更新车辆信息
     *
     * @param car
     */
    @Override
    public void updateCar(Car car) {
        baseMapper.updateById(car);
    }

    /**
     * 获取车辆信息
     *
     * @param id
     * @return
     */
    @Override
    public CarVo getCarById(Long id) {
        Car car = baseMapper.selectById(id);
        // TODO: 2022/9/19 远程调用获取到配送员信息
        String dispatcherName = "";

        return carToCarVo(car, dispatcherName);
    }

    /**
     * 分页条件查询
     * 其中根据是否需要查询司机信息进行远程调用
     *
     * @param page
     * @param limit
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> pageFind(Integer page, Integer limit, CarPageParams params) {
        LambdaQueryWrapper<Car> wrapper = createQueryWrapper(params);
        IPage<Car> iPage = new Page(page, limit);
        baseMapper.selectPage(iPage, wrapper);

        // 构造数据
        List<Car> records = iPage.getRecords();
        List<CarVo> data = new ArrayList<>(records.size());
        Map<String, Object> result = new HashMap<>(4);
        result.put("total", iPage.getTotal());
        result.put("data", data);

        // 车辆分配了司机情况 分配则远程调用填充数据
        if (!ObjectUtils.isEmpty(params)
                && !ObjectUtils.isEmpty(params.getIsDispatch())
                && params.getIsDispatch()) {
            List<Long> disPatcherIds = new ArrayList<>(records.size());

            // TODO: 2022/9/19 远程调用 获取司机姓名
            Map<Long, String> dispatcher = new HashMap<>();
            records.forEach(car -> {
                data.add(carToCarVo(car, dispatcher.get(car.getDispatcherId())));
            });
        }

        records.forEach(car -> {
            data.add(carToCarVo(car, null));
        });

        return result;
    }

    /**
     * 车辆维修操作
     *
     * @param id
     */
    @Override
    public void repair(Long id) {
        Car car = baseMapper.selectById(id);
        if (!ObjectUtils.isEmpty(car.getDispatcherId())) {
            // TODO: 2022/9/19 远程调用 终止司机和车辆的使用联系

        }

        // TODO: 2022/9/19 远程调用 生成维修操作

        car.setState(0);
        car.setDispatcherId(null);
        baseMapper.updateById(car);

    }


    /**
     * 生成条件查询对象
     *
     * @param params
     * @return
     */
    private LambdaQueryWrapper<Car> createQueryWrapper(CarPageParams params) {
        LambdaQueryWrapper<Car> wrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(params)) {
            if (!StringUtils.isEmpty(params.getName())) {
                wrapper.like(Car::getName, params.getName());
            }
            if (!ObjectUtils.isEmpty(params.getStartTime())) {
                wrapper.lt(Car::getStartUseTime, params.getStartTime());
            }
            if (!ObjectUtils.isEmpty(params.getEndTime())) {
                wrapper.gt(Car::getStartUseTime, params.getEndTime());
            }
            if (!ObjectUtils.isEmpty(params.getIsDispatch())) {
                if (params.getIsDispatch()) {
                    wrapper.isNotNull(Car::getDispatcherId);
                    return wrapper;
                }

                wrapper.isNull(Car::getDispatcherId);
            }

            return wrapper;
        }

        return null;
    }

    /**
     * 转化 car 为 vo 对象 需要计算出车辆的使用时间
     *
     * @param car
     * @param dispatcherName 配送员名称
     * @return
     */
    private CarVo carToCarVo(Car car, String dispatcherName) {
        // 生成使用天数
        Integer useTime = Math.toIntExact(TimeUtils.toDays(new Date().getTime() - car.getStartUseTime().getTime()));

        return CarVo.builder()
                .useTime(useTime)
                .name(car.getName())
                .state(car.getState())
                .carId(car.getCarId())
                .id(car.getId().toString())
                .dispatcherName(dispatcherName)
                .dispatcherId(car.getDispatcherId().toString())
                .startUseTime(DateFormatUtils.dateFormat(car.getStartUseTime()))
                .build();
    }

}
