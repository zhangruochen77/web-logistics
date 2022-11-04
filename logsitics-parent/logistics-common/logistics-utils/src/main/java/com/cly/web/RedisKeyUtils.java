package com.cly.web;

import java.util.concurrent.TimeUnit;

/**
 * redis 生成 key time 等信息的工具类
 */
public final class RedisKeyUtils {

    private static final String ADMIN_KEY = "admin:";
    public static final long ADMIN_EXP_TIME = 1800L;
    public static final TimeUnit ADMIN_EXP_TIME_UTIL = TimeUnit.SECONDS;

    private static final String PHONE_KEY = "phone:";
    public static final long PHONE_EXP_TIME = 300L;
    public static final TimeUnit PHONE_EXP_TIME_UTIL = TimeUnit.SECONDS;

    private static final String USER_KEY = "user:";
    public static final long USER_EXP_TIME = 180_0000L;
    public static final TimeUnit USER_EXP_TIME_UTIL = TimeUnit.SECONDS;


    /**
     * 生成 admin key 的方法
     *
     * @param id
     * @return
     */
    public static final String createAdminKey(Long id) {
        return ADMIN_KEY + id;
    }

    /**
     * 生成 phone key 的方法
     *
     * @param phone
     * @return
     */
    public static final String createPhoneKey(String phone) {
        return PHONE_KEY + phone;
    }

    /**
     * 生成 user key 的方法
     *
     * @param id
     * @return
     */
    public static final String createUserKey(Long id) {
        return USER_KEY + id;
    }
}
