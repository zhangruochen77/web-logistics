package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.InOrderMapper;
import com.cly.pojo.warehouse.Goods;
import com.cly.pojo.warehouse.InOrder;
import com.cly.service.GoodsService;
import com.cly.service.InOrderService;
import com.cly.vo.warehouse.InOrderQueryVo;
import com.cly.vo.warehouse.InOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InOrderServiceImpl extends ServiceImpl<InOrderMapper, InOrder>
        implements InOrderService {

    @Autowired
    private GoodsService goodsService;

    /**
     * 进货操作 添加进货记录
     *
     * @param id
     * @param number
     * @param adminId
     * @return
     */
    @Override
    public int addRecord(Long id, Integer number, Long adminId) {
        InOrder order = new InOrder();
        order.setAdminId(adminId);
        order.setGoodsId(id);
        order.setInTime(new Date());
        order.setNumber(number);
        return baseMapper.insert(order);
    }

    /**
     * 分页查找进货订单信息
     *
     * @param page
     * @param limit
     * @param vo
     * @return
     */
    @Override
    public Map<String, Object> pageFind(Integer page, Integer limit, InOrderQueryVo vo) {
        LambdaQueryWrapper<InOrder> wrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(vo)) {
            if (!StringUtils.isEmpty(vo.getGoodsId())) {
                wrapper.eq(InOrder::getAdminId, Long.parseLong(vo.getGoodsId()));
            }

            if (!StringUtils.isEmpty(vo.getAdminId())) {
                wrapper.eq(InOrder::getAdminId, Long.parseLong(vo.getAdminId()));
            }

            if (!ObjectUtils.isEmpty(vo.getTimeStart())) {
                wrapper.gt(InOrder::getInTime, vo.getTimeStart());
            }

            if (!ObjectUtils.isEmpty(vo.getTimeEnd())) {
                wrapper.lt(InOrder::getInTime, vo.getTimeEnd());
            }
        }

        Page<InOrder> p = new Page<>(page, limit);
        Page<InOrder> inOrderPage = baseMapper.selectPage(p, wrapper);

        List<InOrderVo> list = new ArrayList<>();
        List<Long> goodsIds = new ArrayList<>();
        for (InOrder record : inOrderPage.getRecords()) {
            goodsIds.add(record.getGoodsId());
        }

        // 获取货物名称 和 图片
        Map<Long, Goods> goods = goodsService.searchByIds(goodsIds);

        // TODO: 2022/9/12 远程调用 货物用户名称

        // 填充结果 list
        for (InOrder record : inOrderPage.getRecords()) {
            InOrderVo inOrderVo = new InOrderVo();
            inOrderVo.setId(record.getId());
            inOrderVo.setNumber(record.getNumber());
            inOrderVo.setInTime(record.getInTime());

            Long goodsId = record.getGoodsId();
            inOrderVo.setGoodsId(goodsId);
            inOrderVo.setGoodsName(goods.get(goodsId).getName());
            inOrderVo.setImg(goods.get(goodsId).getImg());


            // TODO: 2022/9/12 填充货物用户名称

            list.add(inOrderVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", inOrderPage.getTotal());
        map.put("size", inOrderPage.getSize());
        map.put("current", inOrderPage.getCurrent());
        map.put("data", list);

        return map;
    }

    /**
     * 删除指定订单信息
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void deleteByIds(Long[] ids) {
        baseMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    /**
     * 根据商品信息连同删除入库信息
     *
     * @param id
     */
    @Override
    public void deleteByGoodsId(Long id) {
        baseMapper.delete(new LambdaQueryWrapper<InOrder>().eq(InOrder::getGoodsId, id));
    }

    /**
     * 批量根据商品信息连同删除入库信息
     *
     * @param ids
     */
    @Override
    public void deleteByGoodsIds(Long[] ids) {
        baseMapper.delete(new LambdaQueryWrapper<InOrder>().in(InOrder::getGoodsId, ids));
    }

}
