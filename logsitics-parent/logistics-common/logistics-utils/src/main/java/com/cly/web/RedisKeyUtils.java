package com.cly.web;

/**
 * redis 生成 key 的工具类
 */
public class RedisKeyUtils {

    /**
     * 生成 admin key 的方法
     *
     * @param id
     * @return
     */
    public static final String createAdminKey(Long id) {
        return "admin:" + id;
    }

    /**
     * 生成 phone key 的方法
     *
     * @param phone
     * @return
     */
    public static String createPhoneKey(String phone) {
        return "phone:" + phone;
    }

    /**
     * 生成 user key 的方法
     *
     * @param id
     * @return
     */
    public static String createUserKey(Long id) {
        return "user:" + id;
    }
}
