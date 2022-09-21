package com.cly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.dao.UserMapper;
import com.cly.exception.LogException;
import com.cly.pojo.admin.User;
import com.cly.service.LoginInfoService;
import com.cly.service.UserService;
import com.cly.vo.admin.RegistryParams;
import com.cly.vo.admin.UserVo;
import com.cly.web.MD5Util;
import com.cly.web.RedisKeyUtils;
import com.cly.web.ThreadLocalAdminUtils;
import com.cly.web.TokenUtils;
import com.cly.web.param.PasswordParams;
import com.cly.web.param.PhoneParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private LoginInfoService loginInfoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户使用密码方式登录
     *
     * @param params 用户账号和密码
     * @return token 信息
     */
    @Override
    public String loginByPass(PasswordParams params) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, params.getUsername()));

        if (ObjectUtils.isEmpty(user)) {
            throw new LogException("账号不存在！");
        }

        String passMd5 = MD5Util.getMD5(params.getPassword());
        if (!user.getPassword().equals(passMd5)) {
            throw new LogException("账号或者密码不正确！");
        }

        // 生成 token
        String token = TokenUtils.createToken(user.getId(), user.getUsername());
        redisTemplate.opsForValue().set(RedisKeyUtils.createUserKey(user.getId()), token, 1L, TimeUnit.HOURS);

        // TODO: 2022/9/21 记录登录记录 多线程处理
        loginInfoService.doLoginLog(user.getId(), false);

        return token;
    }

    /**
     * 使用手机方式登录
     *
     * @param params
     * @return
     */
    @Override
    public String loginByPhone(PhoneParams params) {
        String key = RedisKeyUtils.createPhoneKey(params.getPhone());

        Object code = redisTemplate.opsForValue().get(key);

        if (ObjectUtils.isEmpty(code) || !params.getCode().equals(code.toString())) {
            throw new LogException("短信验证码错误或已失效，请重新获取");
        }

        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, params.getPhone())
                .select(User::getId)
                .select(User::getUsername));

        // 生成 token
        String token = TokenUtils.createToken(user.getId(), user.getUsername());

        redisTemplate.opsForValue().set(RedisKeyUtils.createUserKey(user.getId()), token, 1L, TimeUnit.HOURS);

        // TODO: 2022/9/21 记录登录记录 多线程处理
        loginInfoService.doLoginLog(user.getId(), true);

        return token;
    }

    /**
     * 用户退出登录 删除 token 信息
     * threadLocal 中获取 token
     * token 中获取 id
     * 生成 redis id 删除 redis 中 token 信息
     */
    @Override
    public void logout() {
        redisTemplate.opsForValue().getOperations().delete(RedisKeyUtils.createUserKey(TokenUtils.getId(ThreadLocalAdminUtils.get())));
    }

    /**
     * 用户注册账号
     *
     * @param params
     * @return
     */
    @Override
    public String registry(RegistryParams params) {
        Object code = redisTemplate.opsForValue().get(RedisKeyUtils.createPhoneKey(params.getPhone()));

        if (ObjectUtils.isEmpty(code) || !params.getCode().equals(code.toString())) {
            throw new LogException("短信验证码错误或已失效，请重新获取");
        }

        User user = RegistryParamsCreatUser(params);
        baseMapper.insert(user);

        String token = TokenUtils.createToken(user.getId(), user.getUsername());

        redisTemplate.opsForValue().set(RedisKeyUtils.createUserKey(user.getId()), token, 1L, TimeUnit.HOURS);

        // TODO: 2022/9/21 记录登录记录 多线程处理
        loginInfoService.doLoginLog(user.getId(), true);

        return token;
    }

    /**
     * 获取用户个人信息
     *
     * @return
     */
    @Override
    public UserVo getUserInfo() {
        String token = ThreadLocalAdminUtils.get();
        Long id = TokenUtils.getId(token);
        User user = baseMapper.selectById(id);

        return userToUserVo(user);
    }

    /**
     * 转换用户对象为 vo 对象
     *
     * @param user
     * @return
     */
    private UserVo userToUserVo(User user) {
        UserVo vo = new UserVo();

        vo.setImg(user.getImg());
        vo.setSex(user.getSex());
        vo.setAge(user.getAge());
        vo.setPhone(user.getPhone());
        vo.setAddress(user.getAddress());
        vo.setId(user.getId().toString());
        vo.setNickName(user.getNickName());
        vo.setUsername(user.getUsername());
        if (!ObjectUtils.isEmpty(user.getCity())) {
            vo.setCity(user.getCity().toString());
        }
        if (!ObjectUtils.isEmpty(user.getCity())) {
            vo.setCounty(user.getCounty().toString());
        }
        if (!ObjectUtils.isEmpty(user.getProvince())) {
            vo.setProvince(user.getProvince().toString());
        }

        return vo;
    }


    /**
     * 将注册登录的参数类转化为数据库用户实体类
     *
     * @param params
     * @return
     */
    private User RegistryParamsCreatUser(RegistryParams params) {
        User user = new User();
        user.setPhone(params.getPhone());
        user.setUsername(params.getUsername());
        user.setPassword(MD5Util.getMD5(params.getPassword()));
        user.setImg("https://img.zcool.cn/community/01786557e4a6fa0000018c1bf080ca.png@1280w_1l_2o_100sh.png");

        return user;
    }

}
