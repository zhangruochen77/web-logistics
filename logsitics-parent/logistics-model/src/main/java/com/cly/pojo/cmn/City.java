package com.cly.pojo.cmn;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City extends Base {

    /**
     * 城市名称
     */
    private String value;

    /**
     * 其上级 省级 id
     */
    private Long provinceId;

}
