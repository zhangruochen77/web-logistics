package com.cly.controller;

import com.cly.service.InOrderService;
import com.cly.vo.warehouse.InOrderQueryVo;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/log/warehouse/inOrder")
public class InOrderController {

    @Autowired
    private InOrderService inOrderService;

    /**
     * 分页查询进货订单信息
     *
     * @param page
     * @param limit
     * @param vo
     * @return
     */
    @PostMapping("/pageFind/{page}/{limit}")
    public Result pageFind(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit,
                           @RequestBody InOrderQueryVo vo) {
        Map<String, Object> res = inOrderService.pageFind(page, limit, vo);
        return Result.success(res);
    }

    /**
     * 删除指定进货订单
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        inOrderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteByIds")
    public Result deleteByIds(@RequestBody Long[] ids) {
        inOrderService.deleteByIds(ids);
        return Result.success();
    }

}
