package com.cly.vo.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsQueryVo {

    /**
     * 省 id
     */
    private Long province;

    /**
     * 城市 id
     */
    private Long city;

    /**
     * 区县 id
     */
    private Long county;

    /**
     * 名称
     */
    private String name;

    /**
     * 价格起始区间
     */
    private Double priceStart;

    /**
     * 价格结束区间
     */
    private Double priceEnd;

}
