package com.cly.controller.api;

import com.cly.pojo.admin.User;
import com.cly.service.UserService;
import com.cly.vo.admin.RegistryParams;
import com.cly.web.Result;
import com.cly.web.param.PasswordParams;
import com.cly.web.param.PhoneParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * 前台用户个人信息操作
 */
@RestController
@RequestMapping("/front/admin/user")
public class UserApiController {

    @Autowired
    private UserService userService;


    /**
     * 用户使用密码方式登录
     *
     * @param params 用户名 密码等等参数
     * @return token
     */
    @PostMapping("/loginByPass")
    public Result loginByPass(@RequestBody PasswordParams params) {
        return Result.success(userService.loginByPass(params), 200);
    }

    /**
     * 用户使用手机方式登录
     *
     * @param params 手机 验证码参数
     * @return token 信息
     */
    @PostMapping("/loginByPhone")
    public Result loginByPhone(@RequestBody PhoneParams params) {
        return Result.success(userService.loginByPhone(params), 200);
    }

    /**
     * 用户退出登录 删除其 token 信息
     *
     * @return 仅仅返回成功退出的代码
     */
    @PostMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.success();
    }

    /**
     * 用户注册操作
     *
     * @param params 用户注册参数
     * @return token 信息
     */
    @PostMapping("/registry")
    public Result registry(@RequestBody RegistryParams params) {
        return Result.success(userService.registry(params), 200, "注册成功！");
    }


    /**
     * 用户获取个人的信息
     *
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo() {
        return Result.success(userService.getUserInfo());
    }

    /**
     * 远程调用 获取用户信息
     *
     * @return
     */
    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }


}
