package com.cly.feign;

import com.cly.pojo.admin.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("logistics-admin")
public interface WarehouseAdminFeign {

    /**
     * 多 id 查找用户
     *
     * @param adminIds
     * @return
     */
    @PostMapping("/log/admin/admin/listAdminByArray")
    Map<Long, String> listAdminByArray(@RequestBody Long[] adminIds);


    /**
     * 远程调用 获取用户信息
     *
     * @return
     */
    @GetMapping("/front/admin/user/getUserById/{id}")
    User getUserById(@PathVariable("id") Long id);


}
