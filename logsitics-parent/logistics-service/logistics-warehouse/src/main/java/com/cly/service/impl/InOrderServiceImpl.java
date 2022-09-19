package com.cly.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.InOrderMapper;
import com.cly.feign.WarehouseAdminFeign;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InOrderServiceImpl extends ServiceImpl<InOrderMapper, InOrder>
        implements InOrderService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private WarehouseAdminFeign warehouseAdminFeign;

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

        // 分页查找
        Page<InOrder> p = new Page<>(page, limit);
        Page<InOrder> inOrderPage = baseMapper.selectPage(p, createQueryWrapper(vo));
        Map<String, Object> map = new HashMap<>();

        if (0 != inOrderPage.getRecords().size()) {
            List<InOrder> records = inOrderPage.getRecords();
            map.put("data", createQueryResult(records));
        } else {
            map.put("data", null);
        }

        // 填充响应结果
        map.put("total", inOrderPage.getTotal());
        map.put("size", inOrderPage.getSize());
        map.put("current", inOrderPage.getCurrent());

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

    /**
     * 条件查询生成订单表
     *
     * @param vo
     * @param response
     */
    @Override
    public void exportInOrder(InOrderQueryVo vo, HttpServletResponse response) {
        List<InOrderVo> voList = createQueryResult(baseMapper.selectList(createQueryWrapper(vo)));

        // 设置导出忽略字段
        Set<String> excludeColumnNames = new HashSet<>();
        excludeColumnNames.add("img");
        // 设置响应类型
        response.setContentType("application/vnd.ms-excel; charset=utf-8");
        // 设置字符编码
        response.setCharacterEncoding("utf-8");
        String fileName = "订单入库表";
        response.setHeader("Content-Disposition", "attachment;filename=订单入库表.xlsx");

        try {
            // 写出文件不用调用监听
            // 在读取文件操作的时候才需要使用监听
            EasyExcel.write(response.getOutputStream(), InOrderVo.class)
                    .excludeColumnFiledNames(excludeColumnNames).sheet(fileName).doWrite(voList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 条件查询生成当前页订单表
     *
     * @param page
     * @param limit
     * @param vo
     * @param response
     */
    @Override
    public void exportCurrentInOrder(Integer page, Integer limit, InOrderQueryVo vo, HttpServletResponse response) {
        // 分页查找
        Page<InOrder> p = new Page<>(page, limit);
        Page<InOrder> inOrderPage = baseMapper.selectPage(p, createQueryWrapper(vo));

        List<InOrderVo> voList = createQueryResult(inOrderPage.getRecords());
        response.setContentType("application/vnd.ms-excel; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String filename = "入库订单表";
        response.setHeader("Content-Disposition", "attachment;filename=订单入库表.xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), InOrderVo.class)
                    .sheet(filename).doWrite(voList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现远程调用 将数据填充到 vo 对象当中
     *
     * @param inOrders
     * @return
     */
    private List<InOrderVo> createQueryResult(List<InOrder> inOrders) {
        // 远程调用条件分发查找 使用 map 降低时间复杂度 O ^ 2 -> O
        List<InOrderVo> list = new ArrayList<>();
        List<Long> goodsIds = new ArrayList<>();
        Set<Long> adminIds = new HashSet<>();
        for (InOrder record : inOrders) {
            goodsIds.add(record.getGoodsId());
            adminIds.add(record.getAdminId());
        }

        // 获取货物名称 和 图片
        Map<Long, Goods> goods = goodsService.searchByIds(goodsIds);

        // 远程调用 获取操作员名称
        Map<Long, String> admins = warehouseAdminFeign.listAdminByArray(adminIds.toArray(new Long[adminIds.size()]));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


        // 填充结果 list
        for (InOrder record : inOrders) {
            InOrderVo inOrderVo = new InOrderVo();
            inOrderVo.setId(record.getId().toString());
            inOrderVo.setNumber(record.getNumber());
            inOrderVo.setInTime(sdf.format(record.getInTime()));

            // 填充商品信息
            Long goodsId = record.getGoodsId();
            inOrderVo.setGoodsId(goodsId.toString());
            inOrderVo.setGoodsName(goods.get(goodsId).getName());
            inOrderVo.setImg(goods.get(goodsId).getImg());

            // 填充操作员名称
            inOrderVo.setAdminId(record.getAdminId().toString());
            inOrderVo.setAdminUsername(admins.get(record.getAdminId()));

            list.add(inOrderVo);
        }

        return list;
    }

    /**
     * 生成 query 条件查询对象
     *
     * @param vo
     * @return
     */
    private LambdaQueryWrapper<InOrder> createQueryWrapper(InOrderQueryVo vo) {
        // 条件设置
        LambdaQueryWrapper<InOrder> wrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(vo)) {
            if (!StringUtils.isEmpty(vo.getGoodsId())) {
                wrapper.eq(InOrder::getGoodsId, Long.parseLong(vo.getGoodsId()));
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

        return wrapper;
    }

}
