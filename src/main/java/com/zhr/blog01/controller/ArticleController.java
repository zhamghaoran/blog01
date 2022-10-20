package com.zhr.blog01.controller;

import com.zhr.blog01.common.aop.LogAnnotation;
import com.zhr.blog01.common.cache.Cache;
import com.zhr.blog01.dao.pojo.PostArticleParm;
import com.zhr.blog01.service.ArticleService;
import com.zhr.blog01.vo.params.ArticleVo;
import com.zhr.blog01.vo.params.Result;
import com.zhr.blog01.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//json 数据前后端交互
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "文章",operation = "获取文章列表") // 代表要对此接口记日志
    @Cache(name = "文章列表",expire = 10 * 1000)
    public Result listArticle(@RequestBody PageParams pageParams) {
        List<ArticleVo> articles = articleService.listArticlesPage(pageParams);
        return Result.success(articles);
    }

    @LogAnnotation(module = "最热文章",operation = "获取最热文章列表")
    @PostMapping("hot")
    @Cache(expire = 10 * 60 * 1000,name = "hot_article")
    public Result hotArticle() {
        int lim = 5;
        return articleService.hotArticle(lim);
    }
    @RequestMapping(value = "new",method = RequestMethod.POST)
    public Result NewArticle() {
        return articleService.NewArticle();
    }
    @RequestMapping(value = "listArchives",method = RequestMethod.POST)
    public Result listArchives() {
        return articleService.listArchives();
    }
    @RequestMapping(value = "/view/{id}",method = RequestMethod.POST)
    public Result finArticleById(@PathVariable("id") long id) throws InterruptedException {
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }
    @RequestMapping(value = "publish",method =  RequestMethod.POST)
    public Result PostArticle(@RequestBody PostArticleParm postArticleParm) {
        return articleService.PostArticle(postArticleParm);
    }

}
