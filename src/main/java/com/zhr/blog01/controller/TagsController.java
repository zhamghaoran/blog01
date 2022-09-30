package com.zhr.blog01.controller;

import com.mysql.cj.x.protobuf.MysqlxSession;
import com.zhr.blog01.service.Tagservice;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private Tagservice tagservice;
    @RequestMapping("hot")
    public Result hot() {
        int limit = 6;
        return tagservice.hots(limit);
    }
}
