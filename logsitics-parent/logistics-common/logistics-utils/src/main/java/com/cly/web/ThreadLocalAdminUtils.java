package com.cly.web;

/**
 * ThreadLocal 工具类 用于线程绑定存放用户 token
 * 方便在程序运行过程中随时获取使用用户的个人信息
 * 其通过和 TokenUtils 结合使用
 * 其存放的是 token 信息
 * TokenUtils 可以通过 token 信息获取到用户的 id 和 username 等等相关信息
 */
public class ThreadLocalAdminUtils {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    /**
     * 存放 token 值
     *
     * @param token
     */
    public static void set(String token) {
        threadLocal.set(token);
    }

    /**
     * 获取 token 值
     *
     * @return
     */
    public static String get() {
        return threadLocal.get();
    }

    /**
     * 移除 token 值 防止内存泄露
     */
    public static void remove() {
        threadLocal.remove();
    }

}
