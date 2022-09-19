package com.cly.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目中多处使用到日期格式化 提取处理为工具类
 * 但是由于 SimpleDateFormat 不是线程安全的 存在线程安全问题 不能设置为静态的
 * 使用线程绑定的方式 让每个线程只能调用自己线程的格式化对象 解决线程安全问题
 * 利用空间换取时间操作
 */
public class DateFormatUtils {

    /**
     * 使用本地线程绑定的
     */
    private static final ThreadLocal<SimpleDateFormat> LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

    /**
     * 日期转换为字符串格式
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        return LOCAL.get().format(date);
    }

    /**
     * 转化字符串为日期对象
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return LOCAL.get().parse(date);
    }
}
