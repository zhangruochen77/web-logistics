package com.cly.vo.warehouse;

import com.cly.vo.BaseVo;
import lombok.Data;

@Data
public class GoodsVo extends BaseVo {

    private String province;

    private String city;

    private String county;

    private String name;

    private Double price;

    private String img;

    private String description;
}
