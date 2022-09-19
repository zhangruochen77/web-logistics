package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.GoodsMapper;
import com.cly.feign.WarehouseCmnFeign;
import com.cly.pojo.warehouse.Goods;
import com.cly.service.GoodsService;
import com.cly.service.InOrderService;
import com.cly.vo.warehouse.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends
        ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private InOrderService inOrderService;

    @Autowired
    private WarehouseCmnFeign warehouseCmnFeign;

    /**
     * 分页查询商品
     *
     * @param page
     * @param limit
     * @param goodsQueryVo
     * @return
     */
    @Override
    public Map<String, Object> pageFind(int page, int limit, GoodsQueryVo goodsQueryVo) {

        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper();

        // 设置查询条件
        if (!ObjectUtils.isEmpty(goodsQueryVo)) {

            if (!ObjectUtils.isEmpty(goodsQueryVo.getProvince())) {
                wrapper.eq(Goods::getProvince, goodsQueryVo.getProvince());
            }

            if (!ObjectUtils.isEmpty(goodsQueryVo.getCity())) {
                wrapper.eq(Goods::getCity, goodsQueryVo.getCity());
            }

            if (!ObjectUtils.isEmpty(goodsQueryVo.getCounty())) {
                wrapper.eq(Goods::getCounty, goodsQueryVo.getCounty());
            }

            if (!ObjectUtils.isEmpty(goodsQueryVo.getName())) {
                wrapper.like(Goods::getName, goodsQueryVo.getName());
            }

            if (!ObjectUtils.isEmpty(goodsQueryVo.getPriceStart())) {
                wrapper.gt(Goods::getPrice, goodsQueryVo.getPriceStart());
            }

            if (!ObjectUtils.isEmpty(goodsQueryVo.getPriceEnd())) {
                wrapper.lt(Goods::getPrice, goodsQueryVo.getPriceEnd());
            }
        }

        IPage iPage = new Page(page, limit);
        IPage<Goods> goodsIPage = baseMapper.selectPage(iPage, wrapper);

        Map<String, Object> data = new HashMap<>(4);
        List<GoodsSelectVo> records = goodsToSelectVo(goodsIPage.getRecords());
        data.put("records", records);
        data.put("total", goodsIPage.getTotal());
        return data;
    }


    /**
     * 添加新商品
     *
     * @param goodsVo
     */
    @Override
    public void addGoods(GoodsVo goodsVo) {
        Goods goods = voParseGoods(goodsVo);
        goods.setNumber(0);
        goods.setState(1);
        baseMapper.insert(goods);
    }

    /**
     * 更新商品操作
     *
     * @param goodsVo
     */
    @Override
    public void update(GoodsVo goodsVo) {
        baseMapper.updateById(voParseGoods(goodsVo));
    }

    /**
     * 获取商品 远程调用 cmn 获取省 城市 区县信息
     *
     * @param id
     * @return
     */
    @Override
    public GoodsInfoVo getById(Long id) {
        Goods goods = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(goods)) {
            return null;
        }

        // 远程调用
        Map<String, String> provinceMap = (Map<String, String>) warehouseCmnFeign
                .getProvinceById(goods.getProvince()).getData();
        Map<String, String> cityMap = (Map<String, String>) warehouseCmnFeign
                .getCityById(goods.getCity()).getData();
        Map<String, String> countyMap = (Map<String, String>) warehouseCmnFeign
                .getCountyById(goods.getCounty()).getData();
        String province = provinceMap.get("value");
        String city = cityMap.get("value");
        String county = countyMap.get("value");

        StringBuilder builder = new StringBuilder(province.length() + city.length() + county.length());
        String address = builder.append(province).append(city).append(county).toString();

        return goodsToGoodsInfoVo(goods, address);
    }

    /**
     * 转换 goods 对象为单个展示对象
     *
     * @param goods
     * @param address
     * @return
     */
    private GoodsInfoVo goodsToGoodsInfoVo(Goods goods, String address) {
        GoodsInfoVo vo = new GoodsInfoVo();
        vo.setId(goods.getId().toString());
        vo.setName(goods.getName());
        vo.setNumber(goods.getNumber());
        vo.setPrice(goods.getPrice());
        vo.setImg(goods.getImg());
        vo.setDescription(goods.getDescription());
        vo.setState(goods.getState());
        vo.setAddress(address);
        return vo;
    }

    /**
     * 更新商品数量
     *
     * @param id
     * @param number
     * @return
     */
    @Transactional
    @Override
    public boolean inGoods(Long id, Integer number) {
        Goods goods = baseMapper.selectById(id);
        int old = goods.getNumber();
        goods.setNumber(old + number);
        int row = baseMapper.update(goods, new LambdaQueryWrapper<Goods>()
                .eq(Goods::getNumber, old)
                .eq(Goods::getId, id));


        if (1 == row) {
            // TODO: 2022/9/12 redis 当中获取操作员的 id 值
            Long adminId = 1L;  // 假定的操作员 id

            int res = inOrderService.addRecord(id, number, adminId);
            return 1 == res ? true : false;
        }

        // 进货操作失败 同时有人一起操作表
        return false;
    }

    /**
     * 出售商品
     *
     * @param id
     * @param number
     * @return
     */
    @Transactional
    @Override
    public Boolean outGoods(Long id, Integer number) {
        Goods goods = baseMapper.selectById(id);
        int old = goods.getNumber();
        if (old < number) return false;
        goods.setNumber(old - number);
        int row = baseMapper.update(goods, new LambdaQueryWrapper<Goods>()
                .eq(Goods::getNumber, old)
                .eq(Goods::getId, id));

        // TODO: 2022/9/12 生成出货订单

        return 1 == row ? true : false;
    }

    /**
     * 单个删除操作
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);

        // 删除入库订单信息
        inOrderService.deleteByGoodsId(id);

        // TODO: 2022/9/12 删除出库订单信息
    }

    /**
     * 多个删除操作
     *
     * @param ids
     */
    @Override
    public void deleteBatchByIds(Long[] ids) {
        baseMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));

        // 删除入库订单信息
        inOrderService.deleteByGoodsIds(ids);

        // TODO: 2022/9/12 删除出库订单信息

    }

    /**
     * 批量货物商品信息
     *
     * @param goodsIds
     * @return
     */
    @Override
    public Map<Long, Goods> searchByIds(List<Long> goodsIds) {
        List<Goods> goods = baseMapper.selectBatchIds(goodsIds);

        // 转换 map 类型 减少循环
        Map<Long, Goods> map = new HashMap<>(goods.size() << 1);

        for (int i = 0; i < goods.size(); i++) {
            Goods g = goods.get(i);
            map.put(g.getId(), g);
        }

        return map;
    }

    /**
     * 更新商品上架状态
     */
    @Override
    public void updateState(Long id, Integer state) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setState(state);
        baseMapper.updateById(goods);
    }


    /**
     * 获取所有商品的 id name
     *
     * @return
     */
    @Override
    public List<GoodsIdNameVo> listGoods() {
        List<Goods> goods = baseMapper.selectList(new LambdaQueryWrapper<Goods>().select(Goods::getId, Goods::getName));

        List<GoodsIdNameVo> list = new ArrayList<>(goods.size());
        goods.parallelStream().forEach(g -> list.add(
                new GoodsIdNameVo(g.getId().toString(), g.getName())));
        return list;
    }


    /**
     * 转换 goods 对象为 vo 集合
     *
     * @param records
     * @return
     */
    private List<GoodsSelectVo> goodsToSelectVo(List<Goods> records) {
        List<GoodsSelectVo> list = new ArrayList<>(records.size());
        records.parallelStream().forEach(g -> list.add(goodsToVo(g)));
        return list;
    }


    /**
     * 转换 goods 对象为 select vo
     *
     * @param g
     * @return
     */
    private GoodsSelectVo goodsToVo(Goods g) {
        GoodsSelectVo v = new GoodsSelectVo();
        v.setId(g.getId().toString());
        v.setProvince(g.getProvince().toString());
        v.setCity(g.getCity().toString());
        v.setCounty(g.getCounty().toString());
        v.setName(g.getName());
        v.setNumber(g.getNumber());
        v.setPrice(g.getPrice());
        v.setImg(g.getImg());
        v.setDescription(g.getDescription());
        v.setState(g.getState());

        return v;
    }

    /**
     * goodsVo 转化为 goods 对象
     *
     * @param vo
     * @return
     */
    private Goods voParseGoods(GoodsVo vo) {
        Goods goods = new Goods();

        // 条件判空 防止转化 空指针
        if (!ObjectUtils.isEmpty(vo.getId())) {
            goods.setId(Long.parseLong(vo.getId()));
        }

        if (!StringUtils.isEmpty(vo.getProvince())) {
            goods.setProvince(Long.parseLong(vo.getProvince()));
        }

        if (!StringUtils.isEmpty(vo.getCity())) {
            goods.setCity(Long.parseLong(vo.getCity()));
        }

        if (!StringUtils.isEmpty(vo.getCounty())) {
            goods.setCounty(Long.parseLong(vo.getCounty()));
        }

        goods.setName(vo.getName());
        goods.setPrice(vo.getPrice());
        goods.setDescription(vo.getDescription());
        goods.setImg(vo.getImg());

        return goods;
    }

}
