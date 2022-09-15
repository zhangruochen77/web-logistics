package com.cly.exception;

/**
 * 物流管理平台全局总异常
 */
public class LogException extends RuntimeException {

    public LogException() {
        super();
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(Integer code, String message) {
        super(message);
    }
}
