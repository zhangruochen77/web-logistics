package com.cly.controller;

import com.cly.service.CodeService;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/api/code")
public class CodeController {

    @Autowired
    private CodeService codeService;

    @GetMapping("createCode/{phone}")
    public Result createCode(@PathVariable("phone") String phone) {
        return codeService.createCode(phone);
    }

}
