package com.cly.controller;

import com.cly.service.CarRepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/car/carRepair")
public class CarRepairController {

    @Autowired
    private CarRepairService carRepairService;


}
