package com.cly.service;


import com.cly.pojo.cmn.Role;
import com.cly.web.Result;

import java.util.List;

public interface RoleService {

    /**
     * 通过id获取角色
     */
    Role getRoleById(Long id);

    /**
     * 更新角色信息
     *
     * @param role
     */
    void updateRole(Role role);

    /**
     * 获取所有的角色集合
     *
     * @return
     */
    List<Role> getAllRole();
}
