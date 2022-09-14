package com.cly.vo.warehouse;

import com.cly.vo.BaseVo;
import lombok.Data;

@Data
public class GoodsSelectVo extends BaseVo {

    /**
     * 省 id
     */
    private String province;

    /**
     * 城市 id
     */
    private String city;

    /**
     * 区县 id
     */
    private String county;

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
