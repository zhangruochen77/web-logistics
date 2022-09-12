package com.cly.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

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

}
