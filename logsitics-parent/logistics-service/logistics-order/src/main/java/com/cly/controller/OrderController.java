package com.cly.controller;

import com.cly.service.OrderService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查看新订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageDisNewOrder/{page}/{limit}")
    public Result pageDisNewOrder(@PathVariable("page") Integer page,
                                  @PathVariable("limit") Integer limit) {
        return Result.success(orderService.pageDisNewOrder(page, limit), 200);
    }

}
