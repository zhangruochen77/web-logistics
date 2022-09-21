package com.cly.vo.admin;

import lombok.Data;

/**
 * 用户信息 vo 类
 */
@Data
public class UserVo {

    private String id;

    private String username;

    private String address;

    private String province;

    private String city;

    private String county;

    private String phone;

    private String img;

    private Integer age;

    private Integer sex;

    private String nickName;
}
