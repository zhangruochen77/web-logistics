package com.cly.controller;

import com.cly.service.GoodsService;
import com.cly.vo.warehouse.GoodsQueryVo;
import com.cly.vo.warehouse.GoodsVo;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品接口
 */
@RestController
@RequestMapping("/log/warehouse/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    /**
     * 分页查询商品
     *
     * @param page
     * @param limit
     * @param goodsQueryVo
     * @return
     */
    @PostMapping("/pageFind/{page}/{limit}")
    public Result pageFind(@PathVariable("page") int page,
                           @PathVariable("limit") int limit,
                           @RequestBody GoodsQueryVo goodsQueryVo) {
        return Result.success(goodsService.pageFind(page, limit, goodsQueryVo));
    }


    /**
     * 获取商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(goodsService.getById(id));
    }


    /**
     * 添加新商品
     *
     * @param goodsVo
     * @return
     */
    @PostMapping("/add")
    public Result addGoods(@RequestBody GoodsVo goodsVo) {
        goodsService.addGoods(goodsVo);

        return Result.success();
    }


    /**
     * 更新商品信息
     *
     * @param goodsVo
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody GoodsVo goodsVo) {
        goodsService.update(goodsVo);

        return Result.success();
    }

    /**
     * 更新商品是否上架状态
     *
     * @param state
     * @return
     */
    @PutMapping("/updateState/${id}/${state}")
    public Result updateState(@PathVariable("id") Long id,
                              @PathVariable("state") Integer state) {
        goodsService.updateState(id, state);
        return Result.success();
    }


    /**
     * 单个删除操作
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        goodsService.deleteById(id);
        return Result.success();
    }


    /**
     * 批量删除商品操作
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteBatchByIds(@RequestBody Long[] ids) {
        goodsService.deleteBatchByIds(ids);
        return Result.success();
    }


    /**
     * 新进货物 添加信息
     *
     * @param id
     * @param number
     * @return 返回 true 表示进货成功 false 表示进或失败，其他人同时也在并发的修改这个货物表
     */
    @PutMapping("/inGoods/{id}/{number}")
    public Result inGoods(@PathVariable Long id,
                          @PathVariable Integer number) {
        boolean res = goodsService.inGoods(id, number);
        return res ? Result.success("进货成功！") : Result.fail("进货操作失败");
    }


    /**
     * 出售商品
     *
     * @param id
     * @param number
     * @return 返回 true 则表示商品扣除成功 false 则说明商品不足
     */
    @PutMapping("/outGoods/{id}/{number}")
    public Result outGoods(@PathVariable Long id,
                           @PathVariable Integer number) {
        return Result.success(goodsService.outGoods(id, number));
    }

}
