package com.cly.feign;

import com.cly.vo.admin.UserNamePhone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient("logistics-admin")
public interface OrderAdminFeign {

    /**
     * 批量获取司机的姓名
     *
     * @param ids
     * @return
     */
    @PostMapping("/log/admin/dispatcher/getDispatcherNamesByIds")
    Map<Long, String> getDispatcherNamesByIds(@RequestBody List<Long> ids);


    /**
     * 获取用户信息列表
     *
     * @param ids
     * @return
     */
    @PostMapping("/log/admin/user/listUserByIds")
    Map<Long, String> listUserByIds(@RequestBody Set<Long> ids);


    /**
     * 获取用户的名称和手机号信息
     *
     * @param id 用户主键
     * @return
     */
    @GetMapping("/log/admin/user/getUserNamePhone/{id}")
    UserNamePhone getUserNamePhone(@PathVariable("id") Long id);

    /**
     * 获取单个司机的名字信息
     *
     * @param id
     * @return
     */
    @GetMapping("/log/admin/dispatcher/getDispatcherNameById/{id}")
    String getDispatcherNameById(@PathVariable("id") Long id);
}
