package com.zhr.blog01.controller;

import com.zhr.blog01.service.LoginService;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LogoutController {
    @Autowired
    private LoginService loginService;
    @RequestMapping(method = RequestMethod.GET)
    public Result logout(@RequestHeader("Authorization") String token ) {
        return loginService.logout(token);
    }
}
