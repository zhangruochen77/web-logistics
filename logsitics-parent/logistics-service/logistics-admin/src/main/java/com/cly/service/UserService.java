package com.cly.service;

import com.cly.pojo.admin.User;
import com.cly.vo.admin.RegistryParams;
import com.cly.vo.admin.UserNamePhone;
import com.cly.vo.admin.UserVo;
import com.cly.web.param.PasswordParams;
import com.cly.web.param.PhoneParams;

import java.util.Map;
import java.util.Set;

public interface UserService {

    /**
     * 用户使用密码方式登录
     *
     * @param params 用户账号和密码
     * @return token 信息
     */
    String loginByPass(PasswordParams params);

    /**
     * 用户使用手机方式登录
     *
     * @param params
     * @return
     */
    String loginByPhone(PhoneParams params);

    /**
     * 用户退出登录 删除 token 信息
     */
    void logout();

    /**
     * 用户注册账号
     *
     * @param params
     * @return
     */
    String registry(RegistryParams params);

    /**
     * 获取用户个人信息
     *
     * @return
     */
    UserVo getUserInfo();

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    User getUserById(Long id);


    /**
     * 获取用户信息列表
     *
     * @param ids
     * @return
     */
    Map<Long, String> listUserByIds(Set<Long> ids);

    /**
     * 获取用户的名称和手机号信息
     *
     * @param id 用户主键
     * @return
     */
    UserNamePhone getUserNamePhone(Long id);
}
