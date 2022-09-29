package com.zhr.blog01.service;

import com.zhr.blog01.vo.ArticleVo;
import com.zhr.blog01.vo.PageParams;
import com.zhr.blog01.vo.Result;

import java.util.List;

public interface ArticleService {

    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticlesPage(PageParams pageParams);

}
