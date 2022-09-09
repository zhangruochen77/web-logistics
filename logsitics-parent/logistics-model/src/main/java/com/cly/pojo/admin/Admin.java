package com.cly.pojo.admin;

import com.cly.pojo.base.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends Base {

    /**
     * 员工名称
     */
    private String name;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 登录账户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像图片地址
     */
    private String img;

}
