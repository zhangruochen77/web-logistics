package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.common.DateFormatUtils;
import com.cly.common.TimeUtils;
import com.cly.dao.CarMapper;
import com.cly.exception.LogException;
import com.cly.feign.CarAdminFeign;
import com.cly.pojo.car.Car;
import com.cly.pojo.car.CarRequire;
import com.cly.service.CarRepairService;
import com.cly.service.CarRequireService;
import com.cly.service.CarService;
import com.cly.vo.car.*;
import com.cly.web.ThreadLocalAdminUtils;
import com.cly.web.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    @Autowired
    private CarAdminFeign carAdminFeign;

    @Autowired
    private CarRepairService carRepairService;

    @Autowired
    private CarRequireService carRequireService;

    @Autowired
    private CarService carService;

    @Resource
    private ExecutorService feignExecutorService;

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
        Car car = baseMapper.selectById(id);

        // TODO: 2022/9/20 移除维修信息

        // 远程调用 移除其车辆和司机的信息
        if (!ObjectUtils.isEmpty(car.getDispatcherId())) {
            carAdminFeign.removeCarInfo(car.getDispatcherId());
        }

        baseMapper.deleteById(id);
    }

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
        String dispatcherName = null;

        // 远程调用获取到配送员信息
        if (!ObjectUtils.isEmpty(car.getDispatcherId())) {
            dispatcherName = carAdminFeign.getDispatcherNameById(car.getDispatcherId());
        }

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

        if (iPage.getTotal() == 0L) {
            return result;
        }

        // 车辆分配了司机情况 分配则远程调用填充数据
        if (!ObjectUtils.isEmpty(params) && params.getIsDispatch()
                && !ObjectUtils.isEmpty(params.getIsDispatch())) {

            List<Long> adminIds = new ArrayList<>(records.size());
            records.forEach(car -> adminIds.add(car.getDispatcherId()));

            // 远程调用 获取司机姓名
            Map<Long, String> dispatcher = carAdminFeign.getDispatcherNamesByAdminIds(adminIds);
            records.forEach(car -> {
                data.add(carToCarVo(car, dispatcher.get(car.getDispatcherId())));
            });

            return result;
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
    public void repair(Long id, String description) {
        Car car = baseMapper.selectById(id);

        // 判断条件 远程解除和司机的信息
        if (!ObjectUtils.isEmpty(car.getDispatcherId())) {
            if (!carAdminFeign.removeCarInfo(car.getDispatcherId())) {
                throw new LogException("解除信息失败");
            }
        }

        // 生成维修操作
        carRepairService.repair(id, description);

        baseMapper.update(null,
                new LambdaUpdateWrapper<Car>()
                        .set(Car::getState, 0)
                        .set(Car::getDispatcherId, null)
                        .eq(Car::getId, id));

    }

    /**
     * 获取车辆信息
     *
     * @param id
     * @return
     */
    @Override
    public Car getCar(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 批量获取车辆信息
     *
     * @param carIds
     * @return
     */

    @Override
    public Map<Long, Car> listCarByIds(List<Long> carIds) {
        List<Car> cars = baseMapper.selectBatchIds(carIds);
        Map<Long, Car> result = new HashMap<>(carIds.size() << 1);

        cars.forEach(item -> {
            result.put(item.getId(), item);
        });

        return result;
    }

    /**
     * 解除司机和车辆的关系
     *
     * @param id
     */
    @Override
    public void dissolveRelationship(Long id) {
        Car car = baseMapper.selectById(id);
        Boolean res = carAdminFeign.removeCarInfo(car.getDispatcherId());
        if (!res) {
            throw new LogException("服务器错误");
        }

        int row = baseMapper.update(null, new LambdaUpdateWrapper<Car>().eq(Car::getId, id).set(Car::getDispatcherId, null));
        if (row == 0) {
            throw new LogException("服务器错误");
        }

    }

    /**
     * 添加司机和车辆的关系
     *
     * @param carId
     * @param dispatcherId
     */
    @Override
    public void relateCarAndDispatcher(Long carId, Long dispatcherId) {
        try {
            Boolean res = carAdminFeign.relateCarAndDispatcher(carId, dispatcherId);
            if (!res) {
                throw new LogException("司机已经拥有车辆");
            }
        } catch (LogException e) {
            throw new LogException(e.getMessage());
        }

        baseMapper.update(null, new LambdaUpdateWrapper<Car>().set(Car::getDispatcherId, dispatcherId).eq(Car::getId, carId));
    }

    /**
     * 获取派送员车辆信息
     *
     * @return
     */
    @Override
    public CarDispatcherVo getCar() {
        Long adminId = TokenUtils.getId(ThreadLocalAdminUtils.get());
        Long dispatcherId = carAdminFeign.getDispatcherId(adminId);
        Car car = baseMapper.selectOne(new LambdaQueryWrapper<Car>().eq(Car::getDispatcherId, dispatcherId));

        if (ObjectUtils.isEmpty(car)) {
            return null;
        }

        return carToCarDispatcherVo(car);
    }

    /**
     * 司机分页查找未分配可以使用的车辆信息 状态为 0 并且 dispatcherId is null
     *
     * @param page  第几页
     * @param limit 多少条
     * @return 分页查询结果和总记录条数
     */
    @Override
    public Map<String, Object> pageFindNotDispatchCar(Integer page, Integer limit) {
        LambdaQueryWrapper<Car> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Car::getState, 1).isNull(Car::getDispatcherId);

        IPage<Car> iPage = new Page<>(page, limit);
        baseMapper.selectPage(iPage, wrapper);

        List<CarDispatcherPageVo> data = iPage.getRecords().parallelStream().map(car -> carToCarDispatcherPageVo(car)).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>(4);
        result.put("total", iPage.getTotal());
        result.put("data", data);

        return result;
    }

    /**
     * 派送员申请使用车辆
     *
     * @param id
     * @return
     */
    @Override
    public Boolean requireCar(Long id) {
        Long adminId = TokenUtils.getId(ThreadLocalAdminUtils.get());
        return carRequireService.requireCar(id, adminId);
    }

    /**
     * 获取派送员是否申请了车辆
     *
     * @return
     */
    @Override
    public CarRequireVo getCarRequire() {
        Long dispatcherId = TokenUtils.getId(ThreadLocalAdminUtils.get());
        CarRequire require = carRequireService.getCarRequire(dispatcherId);

        if (ObjectUtils.isEmpty(require)) {
            return null;
        }

        return carRequireToCarRequireVo(require);
    }

    /**
     * 一次性移除多条车辆信息
     *
     * @param carIds 车辆的 id
     * @return
     */
    @Override
    public void listDeleteCars(Long[] carIds) {
        List<Long> carIdList = Arrays.asList(carIds);
        List<Car> cars = baseMapper.selectBatchIds(carIdList);
        Set<Long> dispatcherIds = cars.stream().map(car -> car.getDispatcherId()).collect(Collectors.toSet());

        if (dispatcherIds.size() > 0) {
            Integer count = carAdminFeign.deleteDispatcherByAdminIds(dispatcherIds);

            if (count != dispatcherIds.size()) {
                throw new LogException("服务器发生异常");
            }
        }

        int count = baseMapper.deleteBatchIds(carIdList);
        if (count != carIdList.size()) {
            throw new LogException("服务器发生异常");
        }

    }

    /**
     * 获取车辆申请的数量
     *
     * @return 查表 t_car_require 返回申请总数
     */
    @Override
    public Integer getCarRequireNumber() {
        return carRequireService.getCarRequireNumber();
    }

    /**
     * 查询全部的车辆申请
     *
     * @return 司机和其申请车辆的信息
     */
    @Override
    public List<CarRequirePageVo> getCarRequires() {
        List<CarRequire> requires = carRequireService.listAll();

        // 没有申请 结束返回 返回一个空
        if (requires.size() == 0) {
            return new ArrayList<>();
        }

        // 获取配送员 车辆等信息
        List<Long> adminIds = new ArrayList<>(requires.size());
        List<Long> carIds = new ArrayList<>(requires.size());
        requires.forEach(item -> {
            adminIds.add(item.getAdminId());
            carIds.add(item.getCarId());
        });

        // 通过主表名称获取司机的信息 结合线程池远程调用
        CompletableFuture<List<CarRequirePageVo>> res = CompletableFuture.supplyAsync(() -> {
            return carAdminFeign.getDispatcherNamesByAdminIds(adminIds);
        }, feignExecutorService).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            return carService.listCarByIds(carIds);
        }, feignExecutorService), (dispatchMap, carMap) -> {
            return requires.stream().map(require -> {
                return carRequireToCarRequirePageVo(require, dispatchMap, carMap);
            }).collect(Collectors.toList());
        });

        // 返回结果集
        return res.join();

    }

    /**
     * 同意司机的车辆申请操作
     * 获取司机申请表
     * 判断是否已经删除车辆等等信息
     * 判断车辆是否处于维修状态
     * 更新车辆现在所属于信息以及远程调用更改司机使用车辆信息
     *
     * @param id 申请车辆操作的表的 id
     * @return 操作的结果
     */
    @Override
    @Transactional
    public boolean succeedRequireById(Long id) {
        CarRequire carRequire = carRequireService.getCarRequireById(id);
        if (ObjectUtils.isEmpty(carRequire)) {
            throw new LogException("车辆已经被回收");
        }

        Car car = this.getCar(carRequire.getCarId());
        if (car.getState().equals(0)) {
            throw new LogException("车辆正在维修中");
        }

        // 异步调用执行远程调用 不需要回调结果
        CompletableFuture.runAsync(() -> carAdminFeign.relateCarAndDispatcher(carRequire.getCarId(), carRequire.getAdminId()), feignExecutorService);

        car.setDispatcherId(carRequire.getAdminId());
        this.updateCar(car);

        carRequireService.deleteById(carRequire.getId());

        return true;
    }

    /**
     * 转化司机申请车辆请求为管理员所查看的分页对象
     *
     * @param require     司机申请车辆记录数据
     * @param dispatchMap 司机个人信息 map
     * @param carMap      车辆信息
     * @return CarRequirePageVo 实际查看的分页对象
     */
    private CarRequirePageVo carRequireToCarRequirePageVo(CarRequire require, Map<Long, String> dispatchMap, Map<Long, Car> carMap) {
        Long adminId = require.getAdminId();
        Long carId = require.getCarId();
        Car car = carMap.get(carId);

        CarRequirePageVo vo = new CarRequirePageVo();
        vo.setId(require.getId().toString());
        vo.setCarId(car.getId().toString());
        vo.setCarCarId(car.getCarId());
        vo.setCarName(car.getName());
        vo.setStartUseTime(DateFormatUtils.dateFormat(car.getStartUseTime()));
        vo.setUseTime(userTime(car.getStartUseTime()));
        vo.setDispatcherId(adminId.toString());
        vo.setDispatcherName(dispatchMap.get(adminId));

        return vo;
    }

    /**
     * 转化为派送员所使用的 vo 对象
     *
     * @param require
     * @return
     */
    private CarRequireVo carRequireToCarRequireVo(CarRequire require) {
        Car car = baseMapper.selectById(require.getCarId());

        CarRequireVo vo = new CarRequireVo();

        vo.setId(require.getId().toString());
        vo.setCarId(car.getId().toString());
        vo.setCarCarId(car.getCarId());
        vo.setCarName(car.getName());
        vo.setStartUseTime(DateFormatUtils.dateFormat(car.getStartUseTime()));
        vo.setUseTime(userTime(car.getStartUseTime()));

        return vo;
    }

    /**
     * 将未分配的车辆信息转化为派送员分页查看车辆信息的 vo 对象
     *
     * @param car
     * @return
     */
    private CarDispatcherPageVo carToCarDispatcherPageVo(Car car) {
        // 生成使用天数
        Integer useTime = userTime(car.getStartUseTime());

        CarDispatcherPageVo vo = new CarDispatcherPageVo();
        vo.setId(car.getId().toString());
        vo.setCarId(car.getCarId());
        vo.setName(car.getName());
        vo.setStartUseTime(DateFormatUtils.dateFormat(car.getStartUseTime()));
        vo.setUseTime(useTime);

        return vo;
    }

    /**
     * 将车辆信息转化为管理员个人的车辆信息
     *
     * @param car
     * @return
     */
    private CarDispatcherVo carToCarDispatcherVo(Car car) {
        // 生成使用天数
        Integer useTime = userTime(car.getStartUseTime());

        CarDispatcherVo vo = new CarDispatcherVo();
        vo.setId(car.getId().toString());
        vo.setCarId(car.getCarId());
        vo.setName(car.getName());
        vo.setStartUseTime(DateFormatUtils.dateFormat(car.getStartUseTime()));
        vo.setUseTime(useTime);

        return vo;
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
        Integer useTime = userTime(car.getStartUseTime());

        return CarVo.builder()
                .useTime(useTime)
                .name(car.getName())
                .state(car.getState())
                .carId(car.getCarId())
                .id(car.getId().toString())
                .dispatcherName(dispatcherName)
                .dispatcherId(ObjectUtils.isEmpty(car.getDispatcherId()) ? null : car.getDispatcherId().toString())
                .startUseTime(DateFormatUtils.dateFormat(car.getStartUseTime()))
                .build();
    }

    /**
     * 生成使用天数
     *
     * @param startUseTime
     * @return
     */
    private Integer userTime(Date startUseTime) {
        return Math.toIntExact(TimeUtils.toDays(new Date().getTime() - startUseTime.getTime()));
    }

}
