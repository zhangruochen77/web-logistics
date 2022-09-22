package com.cly.controller.api;

import com.cly.service.OrderService;
import com.cly.web.Result;
import com.cly.web.param.CreateOrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/front/order/order")
public class OrderApiController {

    @Autowired
    private OrderService orderService;


    /**
     * 生成用户订单
     *
     * @param params 商品信息，用户信息，下单数量，收发货地区等等信息
     * @return 订单编号
     */
    @PostMapping("/createOrder")
    public Long createOrder(@RequestBody CreateOrderParams params) {
        return orderService.createOrder(params);
    }


    /**
     * 用户查看自己正在执行中的订单
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageFind/{page}/{limit}")
    public Result pageFind(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit) {
        return Result.success(orderService.userPageFind(page, limit));
    }

}
