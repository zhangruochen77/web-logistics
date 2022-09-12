package com.cly.vo.warehouse;

import lombok.Data;

import java.util.Date;

@Data
public class InOrderVo {

    private Long id;

    private Long goodsId;

    private Integer number;

    private Date inTime;

    private String goodsName;

    private String img;

    private String adminName;

    private Long adminId;
}
