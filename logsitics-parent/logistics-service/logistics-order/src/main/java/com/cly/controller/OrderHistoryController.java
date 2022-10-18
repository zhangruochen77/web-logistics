package com.cly.controller;

import com.cly.service.OrderHistoryService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 管理员分页查看历史订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageFindManage/{page}/{limit}")
    public Result pageFindManage(@PathVariable("page") Integer page,
                                 @PathVariable("limit") Integer limit) {
        return Result.success(orderHistoryService.pageFindManage(page, limit));
    }

}
