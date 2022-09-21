package com.cly.pojo.admin;

import com.cly.pojo.base.Base;
import lombok.Data;

/**
 * 前台用户信息
 */
@Data
public class User extends Base {

    private String username;

    private String address;

    private Long province;

    private Long city;

    private Long county;

    private String password;

    private String phone;

    private String img;

    private Integer age;

    private Integer sex;

    private String nickName;
}
