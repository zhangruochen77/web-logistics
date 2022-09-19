package com.cly.vo.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoVo {

    private String id;

    private String address;

    /**
     * 名称
     */
    private String name;

    private Double price;

    private String img;

    private String description;

    private Integer state;

    private Integer number;
}
