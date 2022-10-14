package com.zhr.blog01.controller;

import com.zhr.blog01.dao.pojo.LoginParam;
import com.zhr.blog01.service.LoginService;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @RequestMapping(method = RequestMethod.POST)
    public Result login( LoginParam loginParam) {
        return loginService.login(loginParam);

    }
}
