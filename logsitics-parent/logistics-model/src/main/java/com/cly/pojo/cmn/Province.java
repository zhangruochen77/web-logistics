package com.cly.pojo.cmn;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province extends Base {

    /**
     * 省名称
     */
    private String value;

}
