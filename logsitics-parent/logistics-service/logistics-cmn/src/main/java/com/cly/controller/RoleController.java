package com.cly.controller;

import com.cly.pojo.cmn.Role;
import com.cly.service.RoleService;
import com.cly.vo.cmn.RoleVo;
import com.cly.web.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 角色信息接口
 */
@RestController
@RequestMapping("/log/cmn/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 通过 id 获取角色信息
     *
     * @param id 主键
     * @return 角色信息
     */
    @GetMapping("getRoleById/{id}")
    public Result getRoleById(@PathVariable("id") Long id) {
        return Result.success(roleService.getRoleById(id));
    }

    /**
     * 更新角色名称等信息
     *
     * @param roleVo
     * @return
     */
    @PutMapping("updateRole")
    public Result updateRole(@RequestBody RoleVo roleVo) {
        Role role = toRole(roleVo);
        roleService.updateRole(role);

        return Result.success();
    }

    /**
     * 获取所有的角色集合
     *
     * @return
     */
    @GetMapping("getAllRole")
    public Result getAllRole() {
        return Result.success(roleService.getAllRole());
    }

    /**
     * 转化 vo 对象为数据库对象
     *
     * @param roleVo vo 对象
     * @return 实际对象
     */
    private Role toRole(RoleVo roleVo) {
        if (ObjectUtils.isEmpty(roleVo)) {
            throw new NullPointerException("需要修改的角色为空");
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        role.setId(Long.parseLong(roleVo.getId()));

        return role;
    }

}
