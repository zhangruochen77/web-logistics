package com.cly.feign;

import com.cly.web.param.CreateOrderParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("logistics-order")
public interface WareHouseOrderFeign {

    /**
     * 生成用户订单
     *
     * @param params 商品信息，用户信息，下单数量，收发货地区等等信息
     * @return 订单编号
     */
    @PostMapping("/front/order/order/createOrder")
    Long createOrder(@RequestBody CreateOrderParams params);

}
