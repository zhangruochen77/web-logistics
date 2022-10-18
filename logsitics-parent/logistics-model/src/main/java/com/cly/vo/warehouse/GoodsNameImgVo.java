package com.cly.vo.warehouse;

import lombok.Data;

/**
 * 仅仅提供商品名称和图片地址的 vo 对象
 */
@Data
public class GoodsNameImgVo {

    private Long id;

    private String name;

    private String img;

}
