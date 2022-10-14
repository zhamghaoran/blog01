package com.zhr.blog01.controller;

import com.zhr.blog01.dao.pojo.LoginParam;
import com.zhr.blog01.service.LoginService;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {
    @Autowired
    private LoginService loginService;
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result register(@RequestBody LoginParam loginParam) {
        return loginService.register(loginParam);
    }
}
