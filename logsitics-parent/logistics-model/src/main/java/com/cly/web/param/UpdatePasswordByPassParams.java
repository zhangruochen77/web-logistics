package com.cly.web.param;

import lombok.Data;

@Data
public class UpdatePasswordByPassParams {

    /**
     * 用户名
     */
    private String username;

    /**
     * 原密码
     */
    private String password;

    /**
     * 新密码
     */
    private String newPassword;

}
