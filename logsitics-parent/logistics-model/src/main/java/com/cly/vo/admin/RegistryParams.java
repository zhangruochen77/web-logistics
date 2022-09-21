package com.cly.vo.admin;

import lombok.Data;

@Data
public class RegistryParams {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}
