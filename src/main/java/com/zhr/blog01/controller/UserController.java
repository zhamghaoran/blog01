package com.zhr.blog01.controller;

import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.service.SysUserService;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;
    @RequestMapping(value = "currentUser",method = RequestMethod.GET)
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.getUserInfo(token);

    }
}
