package com.cly.pojo.admin;

import com.cly.pojo.base.Base;

import java.util.Date;

/**
 * 登录信息记录表
 */
public class LoginInfo extends Base {

    /**
     * 登录用户的 id
     */
    private Long adminId;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 登录时间
     */
    private Date loginTime;

}
