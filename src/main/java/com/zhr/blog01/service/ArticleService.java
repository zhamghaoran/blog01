package com.zhr.blog01.service;

import com.zhr.blog01.vo.params.ArticleVo;
import com.zhr.blog01.vo.params.Result;
import com.zhr.blog01.vo.params.PageParams;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ArticleService {

    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    List<ArticleVo> listArticlesPage(PageParams pageParams);

    Result hotArticle(int lim);

    Result NewArticle();

    Result listArchives();

    ArticleVo findArticleById(long id) throws InterruptedException;
}
