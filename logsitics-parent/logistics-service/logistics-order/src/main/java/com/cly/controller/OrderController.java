package com.cly.controller;

import com.cly.service.OrderService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 分页查看当前派送员正在执行的订单信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageDisDispatcherOrder/{page}/{limit}")
    public Result pageDisDispatcherOrder(@PathVariable("page") Integer page,
                                         @PathVariable("limit") Integer limit) {
        return Result.success(orderService.pageDisDispatcherOrder(page, limit), 200);
    }


    /**
     * 派送员接单
     *
     * @param id
     * @return
     */
    @PutMapping("/orderReceive/{id}")
    public Result orderReceive(@PathVariable("id") Long id) {
        boolean res = orderService.orderReceive(id);
        return res ? Result.success("接单成功！") : Result.fail("接单失败!");
    }


    /**
     * 确认订单送达
     *
     * @param id
     * @return
     */
    @PutMapping("/updateOrderSure/{id}")
    public Result updateOrderSure(@PathVariable("id") Long id) {
        orderService.updateOrderSure(id);
        return Result.success("订单成功送达,等待用户接收");
    }


    /**
     * 一次性接多个单
     *
     * @param orderIds
     * @return
     */
    @PutMapping("/listOrderReceive")
    public Result listOrderReceive(@RequestBody Long[] orderIds) {
        int len = orderService.listOrderReceive(orderIds);
        return len == orderIds.length ? Result.success("全部接单成功") : Result.success("接单成功" + len + "条");
    }

    /**
     * 一次确认多个订单送达
     *
     * @param orderIds
     * @return
     */
    @PutMapping("/listOrderSure")
    public Result listOrderSure(@RequestBody Long[] orderIds) {
        orderService.listOrderSure(orderIds);
        return Result.success("操作成功");
    }


    /**
     * 管理员一次查看多个出库订单 正在执行的订单
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/listOrder/{page}/{limit}")
    public Result listOrder(@PathVariable("page") Integer page,
                            @PathVariable("limit") Integer limit) {
        return Result.success(orderService.listOrder(page, limit));
    }

    /**
     * 获取订单的当前状态下的详细信息
     *
     * @param id 订单编号
     * @return 订单详细信息
     */
    @GetMapping("/order/detail/{id}")
    public Result orderDetailById(@PathVariable("id") Long id) {
        return Result.success(orderService.orderDetailById(id));
    }
}
