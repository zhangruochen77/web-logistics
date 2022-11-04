package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.GoodsMapper;
import com.cly.exception.LogException;
import com.cly.feign.WareHouseOrderFeign;
import com.cly.feign.WarehouseAdminFeign;
import com.cly.feign.WarehouseCmnFeign;
import com.cly.pojo.admin.User;
import com.cly.pojo.warehouse.Goods;
import com.cly.service.GoodsService;
import com.cly.service.InOrderService;
import com.cly.vo.warehouse.*;
import com.cly.web.Result;
import com.cly.web.ThreadLocalAdminUtils;
import com.cly.web.TokenUtils;
import com.cly.web.param.CreateOrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private WarehouseAdminFeign warehouseAdminFeign;

    @Autowired
    private WareHouseOrderFeign wareHouseOrderFeign;

    @Autowired
    private RedisTemplate redisTemplate;

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
            String token = ThreadLocalAdminUtils.get();
            Long userId = TokenUtils.getId(token);
            if (ObjectUtils.isEmpty(userId)) {
                throw new LogException("用户未登录");
            }

            int res = inOrderService.addRecord(id, number, userId);
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
     * 用户分页查找商品数据
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> pageFindGoods(Integer page, Integer limit, GoodsUserParams params) {
        LambdaQueryWrapper<Goods> wrapper = createUserPageFindWrapper(params);
        IPage<Goods> iPage = new Page(page, limit);
        baseMapper.selectPage(iPage, wrapper);

        List<Goods> goods = iPage.getRecords();
        List<GoodsUserPageVo> data = new ArrayList<>(goods.size());
        List<Long> goodIds = new ArrayList<>(goods.size());
        Map<String, Object> result = new HashMap<>(goods.size() << 1);
        result.put("total", iPage.getTotal());
        result.put("data", data);

        goods.forEach(item -> {
            goodIds.add(item.getId());
        });

        // TODO: 2022/9/20 远程调用 获取商品的销售量
        Integer sold = 5;

        goods.forEach(item -> {
            data.add(GoodsToGoodsUserPageVo(item, sold));
        });

        return result;
    }

    /**
     * 用户获取商品的详细信息
     *
     * @param id
     * @return
     */
    @Override
    public GoodsUserDetailsVo getGoodsDetailsById(Long id) {
        Goods goods = baseMapper.selectById(id);

        // TODO: 2022/9/21 远程调用查看订单信息
        Map<String, Object> orderInfo = new HashMap<>();

        // 远程调用查看商品地址信息
        String address = warehouseCmnFeign.getArea(goods.getProvince(),
                goods.getCity(), goods.getCounty());

        return goodsToGoodsUserDetailsVo(goods, address, orderInfo);

    }

    /**
     * 用户下单
     * 首先获取商品的信息
     * 判断商品信息是否满足用户购买的要求 多线程环境下 数据会发生变化
     * 不满足 提醒用户  ——> return
     * 满足 需要保证事务 因为在查询最新数据的过程中 其他用户可能也对该商品做了修改
     * 所以需要在更新商品数量时 使用 cas 算法 这里只运用了一次 cas 算法
     * 没有操作成功 则直接返回给用户 让用户重新下载 如果使用 设定了循环则可能导致 cpu 空转 ——> 但是可能够保证商品能买出 过程中也需要再继续判断是否满足用户购买的要求
     * 当以上的事务操作成功之后 则可以为用户生成订单信息了
     * 那么远程获取用户的信息 商品的信息构造订单产生
     * 最后远程调用订单系统 实际的生成订单信息
     * 最后返回订单编号
     *
     * @param id     商品 id
     * @param number 购买数量
     * @return 订单编号
     */
    @Override
    public Result userDoOrder(Long id, Integer number) {
        Goods goods = baseMapper.selectById(id);
        int oldNumber = goods.getNumber();
        int oldSold = goods.getSold();

        if (oldNumber < number) {
            Result.fail(500, "数量不足");
        }

        // 剩余商品数量
        goods.setNumber(oldNumber - number);
        goods.setSold(oldSold + number);
        int row = baseMapper.update(goods, new LambdaQueryWrapper<Goods>()
                .eq(Goods::getNumber, oldNumber)
                .eq(Goods::getId, id)
                .eq(Goods::getSold, oldSold));

        // 判断事务是否完成 这里不让循环 仅仅只 cas 判断一次 防止 cpu 空转
        if (0 == row) {
            return Result.fail("网络卡顿了,请重试！");
        }

        // 远程调用获取用户信息
        String token = ThreadLocalAdminUtils.get();
        Long userId = TokenUtils.getId(token);
        User user = warehouseAdminFeign.getUserById(userId);

        // 远程调用获取商品发送地址
        String deliver = warehouseCmnFeign.getArea(goods.getProvince(),
                goods.getCity(), goods.getCounty());

        // 构造生成订单的参数
        CreateOrderParams params = new CreateOrderParams();
        params.setReceiveArea(user.getAddress());
        params.setUserId(userId);
        params.setDeliverArea(deliver);
        params.setGoodsId(id);
        params.setNumber(number);

        // 生成出货订单
        Long orderId = wareHouseOrderFeign.createOrder(params);

        return Result.success(orderId.toString(), 200, "下单成功!等待发货");
    }

    /**
     * 多 id 获取商品信息
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, Goods> listGoodsByIds(Set<Long> ids) {
        List<Goods> goods = baseMapper.selectBatchIds(ids);
        Map<Long, Goods> map = new HashMap<>(goods.size() << 1);
        goods.forEach(item -> {
            map.put(item.getId(), item);
        });

        return map;
    }

    /**
     * 多 id 获取商品信息 用于派送员查看订单
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, GoodsDispatcherPageVo> listGoodsDispatcherPageVoByIds(Set<Long> ids) {
        List<Goods> goods = baseMapper.selectBatchIds(ids);
        Map<Long, GoodsDispatcherPageVo> map = new HashMap<>(goods.size() << 1);

        goods.forEach(item -> {
            map.put(item.getId(), toGoodsDispatcherPageVo(item));
        });

        return map;
    }

    /**
     * 返回提供商品名称和图片地址
     *
     * @param id 商品 id
     * @return
     */
    @Override
    public GoodsNameImgVo getOrderNameImgById(Long id) {
        Goods goods = baseMapper.selectById(id);

        return goodsToGoodsNameImgVo(goods);
    }

    /**
     * 转化操作
     *
     * @param goods
     * @return
     */
    private GoodsNameImgVo goodsToGoodsNameImgVo(Goods goods) {
        GoodsNameImgVo vo = new GoodsNameImgVo();
        vo.setId(goods.getId());
        vo.setImg(goods.getImg());
        vo.setName(goods.getName());
        return vo;
    }

    /**
     * 转化为派送员查看订单的操作
     *
     * @param item
     * @return
     */
    private GoodsDispatcherPageVo toGoodsDispatcherPageVo(Goods item) {
        GoodsDispatcherPageVo vo = new GoodsDispatcherPageVo();
        vo.setId(item.getId());
        vo.setDeliverArea(warehouseCmnFeign.getArea(item.getProvince(), item.getCity(), item.getCounty()));
        vo.setImg(item.getImg());
        vo.setName(item.getName());

        return vo;
    }

    /**
     * 转化商品 订单 销售量等等信息为商品的详细信息
     *
     * @param goods     商品信息
     * @param address   商品所在地址信息
     * @param orderInfo 购买了该商品的一些订单信息以及销售量
     * @return
     */
    private GoodsUserDetailsVo goodsToGoodsUserDetailsVo(Goods goods, String address, Map<String, Object> orderInfo) {
        GoodsUserDetailsVo vo = new GoodsUserDetailsVo();
        List<OrderUserAboutInfo> data = (List<OrderUserAboutInfo>) orderInfo.get("data");

        vo.setId(goods.getId().toString());
        vo.setName(goods.getName());
        vo.setSold(goods.getSold());
        vo.setSold(666);
        vo.setDescription(goods.getDescription());
        vo.setImg(goods.getImg());
        vo.setPrice(goods.getPrice().toString());
        vo.setAddress(address);
        vo.setNumber(goods.getNumber());
//        vo.setOrderInfos(data);
        vo.setOrderInfos(null);

        return vo;
    }

    /**
     * 将商品数据转换为 用户分页状态下所展示的数据
     *
     * @param item
     * @param sold
     * @return
     */
    private GoodsUserPageVo GoodsToGoodsUserPageVo(Goods item, Integer sold) {
        GoodsUserPageVo vo = new GoodsUserPageVo();

        vo.setSold(sold);
        vo.setName(item.getName());
        vo.setImg(item.getImg());
        vo.setPrice(item.getPrice());
        vo.setId(item.getId().toString());
        vo.setDescription(item.getDescription());

        return vo;
    }

    /**
     * 用户分页查找商品的条件
     *
     * @param params
     * @return
     */
    private LambdaQueryWrapper<Goods> createUserPageFindWrapper(GoodsUserParams params) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();

        if (!ObjectUtils.isEmpty(params)) {
            if (!ObjectUtils.isEmpty(params.getName())) {
                wrapper.eq(Goods::getName, params.getName());
            }

            if (!ObjectUtils.isEmpty(params.getPriceStart())) {
                wrapper.gt(Goods::getPrice, params.getPriceStart());
            }

            if (!ObjectUtils.isEmpty(params.getPriceEnd())) {
                wrapper.lt(Goods::getPrice, params.getPriceEnd());
            }

        }

        return wrapper;
    }


    /**
     * 转换 goods 对象为 vo 集合
     *
     * @param records
     * @return
     */
    private List<GoodsSelectVo> goodsToSelectVo(List<Goods> records) {
        List<GoodsSelectVo> list = new ArrayList<>(records.size());
        records.parallelStream().forEach(g ->
                list.add(goodsToVo(g))
        );

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
