package com.zhr.blog01.controller;

import com.zhr.blog01.vo.params.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/test")
    public Result test() {
        return Result.success(null);
    }
}
