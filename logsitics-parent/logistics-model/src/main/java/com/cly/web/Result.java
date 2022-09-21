package com.cly.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 与前端交互的响应对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /**
     * 返回的实体数据
     */
    private Object data;

    /**
     * 返回的消息状态码
     */
    private Integer code;

    /**
     * 返回的提示信息
     */
    private String msg;


    /**
     * 成功返回响应结果
     *
     * @return 响应结果
     */
    public static Result success() {
        return new Result(null, 200, "成功！");
    }

    public static Result success(Object data) {
        return new Result(data, 200, "成功！");
    }

    public static Result success(Object data, Integer code) {
        return new Result(data, code, "成功！");
    }

    public static Result success(Object data, Integer code, String msg) {
        return new Result(data, code, msg);
    }

    public static Result success(String msg) {
        return new Result(null, 200, msg);
    }

    public static Result success(Integer code, String msg) {
        return new Result(null, code, msg);
    }


    /**
     * 失败操作返回响应
     *
     * @return 响应结果
     */
    public static Result fail() {
        return new Result(null, 500, "服务器错误!");
    }

    public static Result fail(String msg) {
        return new Result(null, 500, msg);
    }

    public static Result fail(Integer code, String msg) {
        return new Result(null, code, msg);
    }

}
