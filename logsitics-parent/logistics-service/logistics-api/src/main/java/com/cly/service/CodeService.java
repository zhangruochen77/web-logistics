package com.cly.service;

import com.cly.web.Result;

public interface CodeService {

    /**
     * 生成验证码
     *
     * @return
     */
    Result createCode(String phone);

}
