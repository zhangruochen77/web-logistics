package com.cly.vo.warehouse;

import lombok.Data;

/**
 * 用户查询商品参数类
 */
@Data
public class GoodsUserParams {

    private String name;

    private Double priceStart;

    private Double priceEnd;
}
