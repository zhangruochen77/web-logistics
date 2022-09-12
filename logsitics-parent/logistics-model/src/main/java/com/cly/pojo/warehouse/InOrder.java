package com.cly.pojo.warehouse;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InOrder extends Base {

    /**
     * 入库货物 id
     */
    private Long goodsId;

    /**
     * 入库数量
     */
    private Integer number;

    /**
     * 入库时间
     */
    private Date inTime;

    /**
     * 负责人
     */
    private Long adminId;
}
