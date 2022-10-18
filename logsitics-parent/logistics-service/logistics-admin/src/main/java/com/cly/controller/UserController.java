package com.cly.controller;

import com.cly.service.UserService;
import com.cly.vo.admin.UserNamePhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/log/admin/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 获取用户信息列表
     *
     * @param ids
     * @return
     */
    @PostMapping("/listUserByIds")
    public Map<Long, String> listUserByIds(@RequestBody Set<Long> ids) {
        return userService.listUserByIds(ids);
    }


    /**
     * 获取用户的名称和手机号信息
     *
     * @param id 用户主键
     * @return
     */
    @GetMapping("/getUserNamePhone/{id}")
    public UserNamePhone getUserNamePhone(@PathVariable("id") Long id) {
        return userService.getUserNamePhone(id);
    }

}
