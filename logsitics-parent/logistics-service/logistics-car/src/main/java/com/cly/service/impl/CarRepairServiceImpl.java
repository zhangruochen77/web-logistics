package com.cly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.CarRepairMapper;
import com.cly.pojo.car.CarRepair;
import com.cly.service.CarRepairService;
import org.springframework.stereotype.Service;

@Service
public class CarRepairServiceImpl extends ServiceImpl<CarRepairMapper, CarRepair>
        implements CarRepairService {
}
