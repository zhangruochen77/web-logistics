package com.cly.service;

public interface CodeService {

    /**
     * 生成验证码
     *
     * @return
     */
    String createCode(String phone);

}
