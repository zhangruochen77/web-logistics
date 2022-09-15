package com.cly.web.param;

import lombok.Data;

/**
 * 手机方式登录参数列表
 */
@Data
public class PhoneParams {

    private String phone;

    private String code;

}
