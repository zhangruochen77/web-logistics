package com.cly.vo.admin;

import lombok.Data;

@Data
public class AdminVo {

    private String id;
    /**
     * 员工名称
     */
    private String name;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 登录账户名称
     */
    private String username;

    /**
     * 头像图片地址
     */
    private String img;

    /**
     * 手机号码
     */
    private String phone;
}
