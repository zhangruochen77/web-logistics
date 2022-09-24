package com.cly.controller;

import com.cly.service.OrderDispatcherService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log/order/dispatcher")
public class OrderDispatcherController {

    @Autowired
    private OrderDispatcherService orderDispatcherService;

    /**
     * 派送员删除其自身的订单记录
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        orderDispatcherService.deleteById(id);
        return Result.success();
    }

    /**
     * 派送员批量删除自身订单信息记录
     *
     * @param orderIds
     * @return
     */
    @DeleteMapping("/list")
    public Result deleteByIds(@RequestBody Long[] orderIds) {
        orderDispatcherService.deleteByIds(orderIds);
        return Result.success();
    }

}
