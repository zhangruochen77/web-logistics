package com.cly.vo.admin;

import lombok.Data;

/**
 * 更新个人信息的 vo 类
 * 除 id 字段外 都是用户可以自行修改更新的信息
 * 但是修改手机号码之前，需要进行验证码的验证
 */
@Data
public class AdminInfoParams {

    private Long id;

    private String name;

    private String username;

    private String img;

    private String phone;

}
