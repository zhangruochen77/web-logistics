package com.cly.controller;

import com.cly.service.OrderHistoryService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log/order/orderHistory")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    /**
     * 派送员分页查看历史订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageFind/{page}/{limit}")
    public Result pageFind(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit) {
        return Result.success(orderHistoryService.pageFind(page, limit), 200);
    }


}
