package com.cly.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.AdminMapper;
import com.cly.exception.LogException;
import com.cly.pojo.admin.Admin;
import com.cly.pojo.cmn.Role;
import com.cly.service.AdminService;
import com.cly.service.LoginInfoService;
import com.cly.web.MD5Util;
import com.cly.web.TokenUtils;
import com.cly.web.param.PasswordParams;
import com.cly.web.param.PhoneParams;
import com.cly.web.param.UpdatePasswordByPassParams;
import com.cly.web.param.UpdatePasswordByPhoneParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
        implements AdminService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginInfoService loginInfoService;

    /**
     * 使用密码方式登录
     *
     * @param params
     * @return token
     */
    @Override
    public String loginByPassword(PasswordParams params) {
        Admin admin = baseMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, params.getUsername()));
        if (ObjectUtils.isEmpty(admin)) {
            throw new LogException("用户不存在,请联系管理员!");
        }

        String passwordMd5 = MD5Util.getMD5(params.getPassword());

        if (!passwordMd5.equals(admin.getPassword())) {
            throw new LogException("账户或者密码不正确!");
        }

        if (0 == admin.getState()) {
            throw new LogException("账户已经被锁定,请联系管理员!");
        }

        admin.setPassword(null);
        admin.setImg(null);

        String token = TokenUtils.createToken(admin.getId(), admin.getUsername());
        redisTemplate.opsForValue().set("admin:" + admin.getId(),
                JSONObject.toJSONString(admin), 30L, TimeUnit.MINUTES);

        doLoginLog(admin, false);
        return token;
    }


    /**
     * 使用手机号码进行登录
     * redis 中获取验证码进行验证
     * 再通过手机号码获取到用户信息
     * 判断用户现在账户的状态
     * 以上都通过之后 为用户登录生成 token
     * 并且将 token 放入 redis 当中
     * 后面访问需要登录操作后的功能模块时 在网关中进行登录验证
     *
     * @param params
     * @return token
     */
    @Override
    public String loginByPhone(PhoneParams params) {
        Object redisCode = redisTemplate.opsForValue().get(params.getPhone());
        if (ObjectUtils.isEmpty(redisCode)) {
            throw new LogException("验证码已过期,请重新验证");
        }

        if (!redisCode.equals(params.getCode())) {
            throw new LogException("验证码不正确！");
        }

        Admin admin = baseMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getPhone, params.getPhone()));

        if (0 == admin.getState()) {
            throw new LogException("账户已经被锁定,请联系管理员!");
        }

        admin.setPassword(null);
        admin.setImg(null);

        String token = TokenUtils.createToken(admin.getId(), admin.getUsername());
        redisTemplate.opsForValue().set("admin:" + admin.getId(),
                JSONObject.toJSONString(admin), 30L, TimeUnit.MINUTES);

        doLoginLog(admin, true);
        return token;
    }

    /**
     * 通过密码方式更改密码
     *
     * @param params
     * @return
     */
    @Override
    @Transactional
    public Boolean updatePassByPass(UpdatePasswordByPassParams params) {
        String username = params.getUsername();
        String password = params.getPassword();
        String newPassword = params.getNewPassword();

        Admin admin = baseMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));

        if (ObjectUtils.isEmpty(admin)) {
            throw new LogException("服务器异常！");
        }

        // 查看密码是否正确
        String passwordMD5 = MD5Util.getMD5(password);
        if (!passwordMD5.equals(admin.getPassword())) {
            return false;
        }

        newPassword = MD5Util.getMD5(newPassword);
        admin.setPassword(newPassword);
        baseMapper.updateById(admin);

        return true;

    }

    /**
     * 通过手机方式更改密码
     *
     * @param params
     * @return
     */
    @Override
    public Boolean updatePassByPhone(UpdatePasswordByPhoneParams params) {
        Object redisCode = redisTemplate.opsForValue().get(params.getPhone());
        if (ObjectUtils.isEmpty(redisCode)) {
            throw new LogException("验证码已过期");
        }

        if (!redisCode.equals(params)) {
            throw new LogException("验证码不匹配");
        }

        String pass = MD5Util.getMD5(params.getPassword());
        Admin admin = new Admin();
        admin.setPassword(pass);
        baseMapper.update(admin, new LambdaQueryWrapper<Admin>().eq(Admin::getPhone, params.getPhone()));

        return true;
    }

    /**
     * 添加新员工
     * 查看添加新员工的操作者是否拥有权限
     * 默认员工密码为其用户名
     * 手机号可以在创建的时候设置 也可以在员工登录之后自己设置
     *
     * @param admin
     * @return
     */
    @Override
    public boolean addAdmin(Admin admin, String token) {
        Long rootId = TokenUtils.getId(token);
        if (ObjectUtils.isEmpty(rootId)) {
            throw new LogException(601, "账户登录已过期");
        }

        Object redisRoot = redisTemplate.opsForValue().get("admin:" + rootId);
        Admin root = JSONObject.parseObject(redisRoot.toString(), Admin.class);
        Object redisRole = redisTemplate.opsForValue().get("log:cmn:role:" + root.getRoleId());
        Role role = JSONObject.parseObject(redisRole.toString(), Role.class);
        if (!"root".equals(role.getName())) {
            // TODO: 2022/9/15 没有操作权限
        }

        String pass = MD5Util.getMD5(admin.getUsername());
        admin.setPassword(pass);

        return baseMapper.insert(admin) == 1;
    }

    /**
     * 删除员工
     *
     * @param adminId
     * @param token
     * @return
     */
    @Override
    public boolean deleteAdmin(Long adminId, String token) {
        Long rootId = TokenUtils.getId(token);
        if (ObjectUtils.isEmpty(rootId)) {
            throw new LogException(601, "账户登录已过期");
        }

        Object redisRoot = redisTemplate.opsForValue().get("admin:" + rootId);
        Admin root = JSONObject.parseObject(redisRoot.toString(), Admin.class);
        Object redisRole = redisTemplate.opsForValue().get("log:cmn:role:" + root.getRoleId());
        Role role = JSONObject.parseObject(redisRole.toString(), Role.class);
        if (!"root".equals(role.getName())) {
            // TODO: 2022/9/15 没有操作权限
        }

        return baseMapper.deleteById(adminId) == 1;

    }

    /**
     * 做登录记录
     *
     * @param admin   用户信息
     * @param isPhone 是否使用手机登录 false -> 密码登录
     */
    private void doLoginLog(Admin admin, boolean isPhone) {
        loginInfoService.doLoginLog(admin, isPhone);
    }

}
