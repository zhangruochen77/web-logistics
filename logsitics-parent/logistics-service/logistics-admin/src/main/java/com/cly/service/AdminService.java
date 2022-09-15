package com.cly.service;

import com.cly.pojo.admin.Admin;
import com.cly.web.param.PasswordParams;
import com.cly.web.param.PhoneParams;
import com.cly.web.param.UpdatePasswordByPassParams;
import com.cly.web.param.UpdatePasswordByPhoneParams;

public interface AdminService {

    /**
     * 使用手机登录方式
     *
     * @param params
     * @return token
     */
    String loginByPhone(PhoneParams params);

    /**
     * 使用密码方式进行登录
     *
     * @param params
     * @return token
     */
    String loginByPassword(PasswordParams params);

    /**
     * 通过密码方式更改密码
     *
     * @param params
     * @return
     */
    Boolean updatePassByPass(UpdatePasswordByPassParams params);

    /**
     * 通过手机方式更改密码
     *
     * @param params
     * @return
     */
    Boolean updatePassByPhone(UpdatePasswordByPhoneParams params);

    /**
     * 添加新员工
     *
     * @param admin
     * @return
     */
    boolean addAdmin(Admin admin, String token);

    /**
     * 删除员工
     *
     * @param adminId
     * @param token
     * @return
     */
    boolean deleteAdmin(Long adminId, String token);
}
