package com.zhr.blog01.service;

import com.zhr.blog01.dao.mapper.ArticleMapper;
import com.zhr.blog01.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;

public class ThreadService {
    @Async("taskExecutor")
    public void updateViewCount(ArticleMapper articleMapper, Article article) {
        Article articleUpdate = new Article();

    }
}
