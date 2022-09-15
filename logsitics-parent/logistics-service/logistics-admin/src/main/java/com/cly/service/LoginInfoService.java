package com.cly.service;

import com.cly.pojo.admin.Admin;

public interface LoginInfoService {

    /**
     * 做登录记录
     *
     * @param admin   用户信息
     * @param isPhone 使用使用手机登录 false -> 密码登录
     */
    void doLoginLog(Admin admin, boolean isPhone);
}
