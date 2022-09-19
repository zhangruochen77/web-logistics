package com.cly.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final long DAY = 86400000L;

    /**
     * 生成当前时间方法
     *
     * @return
     */
    public static final String createNowTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String format = localDateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
        return format;
    }


    /**
     * 转化时间差为天
     *
     * @param sub
     * @return
     */
    public static Long toDays(long sub) {
        return sub / DAY;
    }
}
