package com.cly;

import com.cly.web.Result;
import org.apache.ibatis.logging.LogException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class LogisticsGlobalExceptionHandler {

    @ExceptionHandler(LogException.class)
    public Result logException(LogException e) {
        e.printStackTrace();
        return Result.fail(500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail(500, e.getMessage());
    }
}
