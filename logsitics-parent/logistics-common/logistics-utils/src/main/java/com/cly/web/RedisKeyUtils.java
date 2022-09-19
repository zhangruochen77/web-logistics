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
}
