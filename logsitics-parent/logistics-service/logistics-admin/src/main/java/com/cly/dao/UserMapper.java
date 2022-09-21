package com.cly.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cly.pojo.admin.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
