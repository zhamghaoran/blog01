package com.zhr.blog01.service;

import com.zhr.blog01.vo.params.CommentParam;
import com.zhr.blog01.vo.params.Result;

public interface CommentService {

    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
