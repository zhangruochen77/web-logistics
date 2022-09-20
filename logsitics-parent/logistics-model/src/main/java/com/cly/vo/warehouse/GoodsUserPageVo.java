package com.cly.vo.warehouse;

import lombok.Data;

/**
 * 用于用于分页展示的 vo 对象
 */
@Data
public class GoodsUserPageVo {

    private String id;

    private String name;

    private Integer sold;

    private String description;

    private String img;

    private Double price;

}
