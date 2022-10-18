package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.CarRequireMapper;
import com.cly.pojo.car.CarRequire;
import com.cly.service.CarRequireService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CarRequireServiceImpl extends ServiceImpl<CarRequireMapper, CarRequire>
        implements CarRequireService {


    /**
     * 派送员申请使用车辆
     * 查看派送员是否已经申请了车辆的使用
     * 如果申请了车辆的使用 那么则更新其申请的车辆
     * 没有申请 则加入申请
     *
     * @param carId   车辆的 id
     * @param adminId 派送员的 id
     * @return
     */
    @Override
    public Boolean requireCar(Long carId, Long adminId) {
        CarRequire carRequireRes = baseMapper.selectOne(new LambdaQueryWrapper<CarRequire>().eq(CarRequire::getAdminId, adminId));

        if (!ObjectUtils.isEmpty(carRequireRes)) {
            carRequireRes.setCarId(carId);
            return baseMapper.updateById(carRequireRes) == 1;
        }

        CarRequire carRequire = new CarRequire();
        carRequire.setCarId(carId);
        carRequire.setAdminId(adminId);
        return baseMapper.insert(carRequire) == 1;
    }


    /**
     * 查看派送员其是否有申请使用车辆的请求
     *
     * @param adminId 派送员的 id
     * @return
     */
    @Override
    public CarRequire getCarRequire(Long adminId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<CarRequire>().eq(CarRequire::getAdminId, adminId));
    }

    /**
     * 获取车辆申请的数量
     *
     * @return 查表 t_car_require 返回申请总数
     */
    @Override
    public Integer getCarRequireNumber() {
        return baseMapper.selectCount(null);
    }

    /**
     * 获取所有车辆申请操作
     *
     * @return 车辆申请操作的集合
     */
    @Override
    public List<CarRequire> listAll() {
        return baseMapper.selectList(null);
    }

    /**
     * 拒绝司机申请车辆的操作
     *
     * @param id 申请车辆操作的表 id
     * @return 操作的结果
     */
    @Override
    public int refuseRequireById(Long id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 根据司机申请表的 id 获取司机获取车辆信息
     *
     * @param id 司机申请表的 id
     * @return 司机申请车辆表的信息
     */
    @Override
    public CarRequire getCarRequireById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 删除该申请操作信息
     *
     * @param id 申请表 id
     * @return 是否成功
     */
    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

}
