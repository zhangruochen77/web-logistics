package com.cly.controller;

import com.cly.pojo.admin.Admin;
import com.cly.service.AdminService;
import com.cly.vo.admin.AdminInfoParams;
import com.cly.web.Result;
import com.cly.web.param.PasswordParams;
import com.cly.web.param.PhoneParams;
import com.cly.web.param.UpdatePasswordByPassParams;
import com.cly.web.param.UpdatePasswordByPhoneParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/log/admin/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 通过手机方式进行登录
     *
     * @return
     */
    @PostMapping("/login/phone")
    public Result loginByPhone(@RequestBody PhoneParams params) {
        String token = adminService.loginByPhone(params);
        return Result.success(token, 200, "登录成功！");
    }

    /**
     * 通过验证码方式进行登录
     *
     * @return
     */
    @PostMapping("/login/password")
    public Result loginByPassword(@RequestBody PasswordParams passwordParams) {
        String token = adminService.loginByPassword(passwordParams);
        return Result.success(token, 200, "登录成功！");
    }

    /**
     * 通过密码方式更改密码
     *
     * @return
     */
    @PutMapping("/updatePassByPass")
    public Result updatePassByPass(@RequestBody UpdatePasswordByPassParams params) {
        Boolean res = adminService.updatePassByPass(params);
        return res ? Result.success(200, "密码更新成功！") : Result.success(567, "密码更新失败！");
    }


    /**
     * 通过手机方式更改密码
     *
     * @param params
     * @return
     */
    @PutMapping("/updatePassByPhone")
    public Result updatePassByPhone(@RequestBody UpdatePasswordByPhoneParams params) {
        Boolean res = adminService.updatePassByPhone(params);
        return res ? Result.success(200, "密码更新成功！") : Result.success(567, "密码更新失败！");
    }

    /**
     * 添加新员工
     *
     * @param admin
     * @param request
     * @return
     */
    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean res = adminService.addAdmin(admin, token);
        return res ? Result.success("添加成功!") : Result.fail("添加失败！");
    }

    /**
     * 员工离职 删除员工信息
     *
     * @param adminId
     * @param request
     * @return
     */
    @DeleteMapping("/deleteAdmin/{adminId}")
    public Result deleteAdmin(@PathVariable("adminId") Long adminId, HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean res = adminService.deleteAdmin(adminId, token);
        return res ? Result.success("删除成功!") : Result.fail("删除失败!");
    }

    /**
     * 获取个人信息
     *
     * @return
     */
    @GetMapping("/getPersonInfo")
    public Result getAdmin() {
        return Result.success(adminService.getAdmin());
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/logOut")
    public Result logOut() {
        adminService.logOut();
        return Result.success();
    }

    /**
     * 用户更新个人信息
     *
     * @param params
     * @return
     */
    @PutMapping("/updateInfo")
    public Result updateInfo(@RequestBody AdminInfoParams params) {
        boolean res = adminService.updateInfo(params);
        return res ? Result.success(200, "更新成功！") : Result.fail("更新失败");
    }

    /**
     * 查看所有管理员信息
     *
     * @return
     */
    @GetMapping("/listAdmin")
    public Result listAdmin() {
        return Result.success(adminService.listAdmin());
    }


    /**
     * 多 id 查找用户
     *
     * @param adminIds
     * @return
     */
    @PostMapping("/listAdminByArray")
    public Map<Long, String> listAdminByArray(@RequestBody Long[] adminIds) {
        return adminService.listAdminByArray(adminIds);
    }

}
