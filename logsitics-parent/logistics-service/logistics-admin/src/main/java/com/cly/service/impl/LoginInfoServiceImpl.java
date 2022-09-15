package com.cly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.LoginInfoMapper;
import com.cly.pojo.admin.Admin;
import com.cly.pojo.admin.LoginInfo;
import com.cly.service.LoginInfoService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo>
        implements LoginInfoService {

    private static final String PHONE = "手机";

    private static final String PASSWORD = "密码";

    /**
     * 做登录记录
     *
     * @param admin   用户信息
     * @param isPhone 使用使用手机登录 false -> 密码登录
     */
    @Override
    public void doLoginLog(Admin admin, boolean isPhone) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setAdminId(admin.getId());
        loginInfo.setLoginTime(new Date());
        loginInfo.setLoginType(isPhone ? PHONE : PASSWORD);

        baseMapper.insert(loginInfo);
    }

}
