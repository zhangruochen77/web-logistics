package com.cly.service;

import com.cly.vo.warehouse.InOrderQueryVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface InOrderService {

    /**
     * 进货操作 添加进货记录
     *
     * @param id
     * @param number
     * @param adminId
     * @return
     */
    int addRecord(Long id, Integer number, Long adminId);

    /**
     * 分页查询订单信息
     *
     * @param page
     * @param limit
     * @param vo
     * @return
     */
    Map<String, Object> pageFind(Integer page, Integer limit, InOrderQueryVo vo);

    /**
     * 删除指定订单信息
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteByIds(Long[] ids);

    /**
     * 根据商品信息连同删除入库信息
     *
     * @param id
     */
    void deleteByGoodsId(Long id);

    /**
     * 批量根据商品信息连同删除入库信息
     *
     * @param ids
     */
    void deleteByGoodsIds(Long[] ids);

    /**
     * 条件查询生成订单表
     *
     * @param vo
     * @param response
     */
    void exportInOrder(InOrderQueryVo vo, HttpServletResponse response);

    /**
     * 条件查询生成当前页订单表
     *
     * @param page
     * @param limit
     * @param vo
     * @param response
     */
    void exportCurrentInOrder(Integer page, Integer limit, InOrderQueryVo vo, HttpServletResponse response);
}
