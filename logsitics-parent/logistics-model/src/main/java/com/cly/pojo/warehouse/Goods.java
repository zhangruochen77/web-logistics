package com.cly.pojo.warehouse;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods extends Base {

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
     * 货物名称
     */
    private String name;

    /**
     * 货物还有数量
     */
    private Integer number;

    /**
     * 货物价格
     */
    private Double price;

    /**
     * 货物图片
     */
    private String img;

    /**
     * 货物描述
     */
    private String description;

    /**
     * 货物状态
     */
    private int state;

}
