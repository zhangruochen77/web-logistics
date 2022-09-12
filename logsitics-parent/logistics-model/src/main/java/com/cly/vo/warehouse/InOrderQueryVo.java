package com.cly.vo.warehouse;

import lombok.Data;

import java.util.Date;

@Data
public class InOrderQueryVo {

    private String goodsId;

    private String adminId;

    private Date timeStart;

    private Date timeEnd;

}
