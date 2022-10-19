package com.zhr.blog01.controller;

import com.zhr.blog01.service.Tagservice;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private Tagservice tagservice;

    @RequestMapping(method = RequestMethod.GET)
    public Result getTag() {
        return tagservice.getAllTags();
    }
    @RequestMapping("detail")
    public Result getDetail() {
        return tagservice.getDetail();
    }

}
