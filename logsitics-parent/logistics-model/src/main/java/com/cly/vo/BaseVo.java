package com.cly.vo;

import lombok.Data;

/**
 * 接收前端参数基本的 vo 参数
 * 因为数据库 id 通用为 Long 类型
 * 前端传这么大的数字时会丢值
 * 需要使用字符串类型进行接收
 */
@Data
public class BaseVo {

    /**
     * 通用接收 id
     */
    private String id;
}
