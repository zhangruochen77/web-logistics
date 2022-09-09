package com.cly.pojo.cmn;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends Base {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色中文名称
     */
    private String nameZh;

}
