package com.cly.web.param;

import lombok.Data;

/**
 * 密码登录方式验证
 */
@Data
public class PasswordParams {

    private String username;

    private String password;

}
