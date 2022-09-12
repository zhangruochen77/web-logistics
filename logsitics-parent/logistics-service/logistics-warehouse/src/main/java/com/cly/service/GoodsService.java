package com.cly.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cly.pojo.warehouse.Goods;
import com.cly.vo.warehouse.GoodsQueryVo;
import com.cly.vo.warehouse.GoodsVo;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    /**
     * 分页查询商品
     *
     * @param page
     * @param limit
     * @param goodsQueryVo
     * @return
     */
    IPage<Goods> pageFind(int page, int limit, GoodsQueryVo goodsQueryVo);

    /**
     * 添加新商品
     *
     * @param goodsVo
     * @return
     */
    void addGoods(GoodsVo goodsVo);

    /**
     * 更新商品操作
     *
     * @param goodsVo
     */
    void update(GoodsVo goodsVo);

    /**
     * 获取商品
     *
     * @param id
     * @return
     */
    Goods getById(Long id);

    /**
     * 新进货物 修改信息
     *
     * @param id
     * @param number
     * @return
     */
    boolean inGoods(Long id, Integer number);

    /**
     * 出售商品
     *
     * @param id
     * @param number
     * @return
     */
    Boolean outGoods(Long id, Integer number);

    /**
     * 单个删除操作
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 多个同时删除操作
     *
     * @param ids
     */
    void deleteBatchByIds(Long[] ids);

    /**
     * 批量获取商品信息
     *
     * @param goodsIds
     * @return
     */
    Map<Long, Goods> searchByIds(List<Long> goodsIds);
}
