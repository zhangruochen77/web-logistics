package com.cly.vo.warehouse;

import lombok.Data;

/**
 * 远程调用用于传输 商品数据
 */
@Data
public class GoodsDispatcherPageVo {

    private Long id;

    private String name;

    private String img;

    private String deliverArea;

}
