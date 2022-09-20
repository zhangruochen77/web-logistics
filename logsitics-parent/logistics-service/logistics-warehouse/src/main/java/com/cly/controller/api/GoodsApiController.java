package com.cly.controller.api;

import com.cly.service.GoodsService;
import com.cly.vo.warehouse.GoodsUserParams;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 前台接口
 */
@RestController
@RequestMapping("/front/warehouse/goods")
public class GoodsApiController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 用户分页查找商品数据
     *
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/pageFindGoods/{page}/{limit}")
    public Result pageFindGoods(@PathVariable("page") Integer page,
                                @PathVariable("limit") Integer limit,
                                @RequestBody GoodsUserParams params) {
        return Result.success(goodsService.pageFindGoods(page, limit, params));
    }
}
