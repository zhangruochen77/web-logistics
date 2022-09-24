package com.cly.controller.api;

import com.cly.service.OrderHistoryService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/front/order/orderHistory")
public class OrderHistoryApiController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    /**
     * 用户查看其历史订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageFind/{page}/{limit}")
    public Result pageFind(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit) {

        return Result.success(orderHistoryService.userPageFind(page, limit));
    }

}
