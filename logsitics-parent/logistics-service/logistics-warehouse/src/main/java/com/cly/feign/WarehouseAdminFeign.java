package com.cly.feign;

import org.springframework.cloud.openfeign.FeignClient;
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

}
