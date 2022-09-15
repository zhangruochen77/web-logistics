package com.cly.controller;

import com.cly.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/admin/loginInfo")
public class LoginInfoController {

    @Autowired
    private LoginInfoService loginInfoService;


}
