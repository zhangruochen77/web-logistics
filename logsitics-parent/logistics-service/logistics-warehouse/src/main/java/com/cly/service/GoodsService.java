package com.cly.service;

import com.cly.pojo.warehouse.Goods;
import com.cly.vo.warehouse.*;
import com.cly.web.Result;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GoodsService {

    /**
     * 分页查询商品
     *
     * @param page
     * @param limit
     * @param goodsQueryVo
     * @return
     */
    Map<String, Object> pageFind(int page, int limit, GoodsQueryVo goodsQueryVo);

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
    GoodsInfoVo getById(Long id);

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

    /**
     * 更新商品上架状态
     */
    void updateState(Long id, Integer state);

    /**
     * 获取所有商品的 id name
     *
     * @return
     */
    List<GoodsIdNameVo> listGoods();

    /**
     * 用户分页查找商品数据
     *
     * @param page
     * @param limit
     * @return
     */
    Map<String, Object> pageFindGoods(Integer page, Integer limit, GoodsUserParams params);

    /**
     * 用户查看商品详细信息情况
     *
     * @param id
     * @return
     */
    GoodsUserDetailsVo getGoodsDetailsById(Long id);

    /**
     * 用户下单
     *
     * @param id
     * @param number
     * @return
     */
    Result userDoOrder(Long id, Integer number);

    /**
     * 多 id 获取商品信息
     *
     * @param ids
     * @return
     */
    Map<Long, Goods> listGoodsByIds(Set<Long> ids);


    /**
     * 多 id 获取商品信息 用于派送员查看订单
     *
     * @param ids
     * @return
     */
    Map<Long, GoodsDispatcherPageVo> listGoodsDispatcherPageVoByIds(Set<Long> ids);

    /**
     * 返回提供商品名称和图片地址
     *
     * @param id 商品 id
     * @return
     */
    GoodsNameImgVo getOrderNameImgById(Long id);

}
