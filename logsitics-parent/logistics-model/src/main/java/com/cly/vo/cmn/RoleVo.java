package com.cly.vo.cmn;

import com.cly.vo.BaseVo;
import lombok.Data;

/**
 * 角色 vo
 */
@Data
public class RoleVo extends BaseVo {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色中文名称
     */
    private String nameZh;
}
