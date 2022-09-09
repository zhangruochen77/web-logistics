package com.cly.pojo.cmn;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class County extends Base {

    /**
     * 区县名称
     */
    private String value;

    /**
     * 上级 市级 id
     */
    private Long cityId;

    /**
     *
     */
}
