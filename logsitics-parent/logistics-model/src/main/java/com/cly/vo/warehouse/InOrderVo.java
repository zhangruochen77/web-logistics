package com.cly.vo.warehouse;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InOrderVo {

    @ExcelProperty(value = "订单编号", index = 0)
    private String id;

    @ExcelProperty(value = "商品编号", index = 1)
    private String goodsId;

    @ExcelProperty(value = "入库数量", index = 2)
    private Integer number;

    @ExcelProperty(value = "入库时间", index = 3)
    private String inTime;

    @ExcelProperty(value = "商品名称", index = 4)
    private String goodsName;

    @ExcelProperty(value = "操作员", index = 5)
    private String adminUsername;

    @ExcelProperty(value = "操作员编号", index = 6)
    private String adminId;

    /**
     * 导出表时忽略该字段
     */
    @ExcelIgnore
    private String img;
}
