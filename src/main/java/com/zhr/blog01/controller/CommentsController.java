package com.zhr.blog01.controller;

import com.zhr.blog01.dao.pojo.Comment;
import com.zhr.blog01.service.CommentService;
import com.zhr.blog01.vo.params.CommentParam;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @RequestMapping("/comments/article/{id}")
    public Result comments(@PathVariable Long id) {
        return commentService.commentsByArticleId(id);
    }

    @RequestMapping("/comments/create/change")
    public Result comment(@RequestBody CommentParam commentParam) {
        return commentService.comment(commentParam);

    }
}
