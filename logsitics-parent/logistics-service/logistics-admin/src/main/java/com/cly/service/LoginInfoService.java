package com.cly.service;

public interface LoginInfoService {

    /**
     * 做登录记录
     *
     * @param id      用户信息
     * @param isPhone 使用使用手机登录 false -> 密码登录
     */
    void doLoginLog(Long id, boolean isPhone);
}
