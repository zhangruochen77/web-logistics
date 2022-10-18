package com.cly.controller;

import com.cly.pojo.warehouse.Goods;
import com.cly.service.GoodsService;
import com.cly.vo.warehouse.GoodsNameImgVo;
import com.cly.vo.warehouse.GoodsDispatcherPageVo;
import com.cly.vo.warehouse.GoodsQueryVo;
import com.cly.vo.warehouse.GoodsVo;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

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
     * 提供给远程调用获取单个商品信息的接口
     *
     * @param id 商品 id
     * @return 商品详细信息
     */
    @GetMapping("/getOrderById/{id}")
    public GoodsNameImgVo getOrderNameImgById(@PathVariable("id") Long id) {
        return goodsService.getOrderNameImgById(id);
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
    @PutMapping("/updateState/{id}/{state}")
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

    /**
     * 获取所有商品信息 id name
     *
     * @return
     */
    @GetMapping("/listGoods")
    public Result listGoods() {
        return Result.success(goodsService.listGoods());
    }


    /**
     * 批量获取商品通过 id
     *
     * @param ids
     * @return
     */
    @PostMapping("/listGoodsByIds")
    public Map<Long, Goods> listGoodsByIds(@RequestBody Set<Long> ids) {
        return goodsService.listGoodsByIds(ids);
    }

    /**
     * 批量获取商品通过 id 用于派送员展示
     *
     * @param ids
     * @return
     */
    @PostMapping("/listGoodsDispatcherPageVoByIds")
    public Map<Long, GoodsDispatcherPageVo> listGoodsDispatcherPageVoByIds(@RequestBody Set<Long> ids) {
        return goodsService.listGoodsDispatcherPageVoByIds(ids);
    }

}
