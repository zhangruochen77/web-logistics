package com.cly.web.param;

import lombok.Data;

@Data
public class
UpdatePasswordByPhoneParams {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;
}
