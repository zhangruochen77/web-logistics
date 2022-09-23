package com.cly.feign;

import com.cly.pojo.warehouse.Goods;
import com.cly.vo.warehouse.GoodsDispatcherPageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient("logistics-warehouse")
public interface OrderWarehouseFeign {


    /**
     * 批量获取商品通过 id
     *
     * @param ids
     * @return
     */
    @PostMapping("/log/warehouse/goods/listGoodsByIds")
    Map<Long, Goods> listGoodsByIds(@RequestBody Set<Long> ids);


    /**
     * 批量获取商品通过 id 用于派送员展示
     *
     * @param ids
     * @return
     */
    @PostMapping("/log/warehouse/goods/listGoodsDispatcherPageVoByIds")
    Map<Long, GoodsDispatcherPageVo> listGoodsDispatcherPageVoByIds(@RequestBody Set<Long> ids);
}
