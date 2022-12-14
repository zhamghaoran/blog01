package com.zhr.blog01.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhr.blog01.dao.mapper.ArticleBodyMapper;
import com.zhr.blog01.dao.mapper.ArticleMapper;
import com.zhr.blog01.dao.mapper.ArticleTagMapper;
import com.zhr.blog01.dao.pojo.*;
import com.zhr.blog01.service.*;
import com.zhr.blog01.vo.params.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Tagservice tagservice;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 分页查询数据库表
     *
     * @param pageParams
     * @return
     */
    @Override
    public List<ArticleVo> listArticlesPage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryChainWrapper = new LambdaQueryWrapper<>();
        // order by 创建时间
        // 是否置顶进行排序
        if (pageParams.getCategoryId() != null) {
            queryChainWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        queryChainWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryChainWrapper);
        List<Article> records = articlePage.getRecords();
        // 能直接返回吗
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagservice.findTagsbyArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory) {
            CategoryVo categoryVo = findCategory(article.getCategoryId());
            articleVo.setCategoryVo(categoryVo);
        }
        return articleVo;
    }

    @Autowired
    private CategoryService categoryService;

    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public Result hotArticle(int lim) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + lim);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result NewArticle() {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.orderByDesc(Article::getCreateDate);
        List<Article> articles = articleMapper.selectList(articleLambdaQueryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        LambdaQueryWrapper<Object> queryWrapper = new LambdaQueryWrapper<>();
        List<Long> list = articleMapper.select();
        List<Archives> archives = new ArrayList<>();
        for (Long i : list) {
            DateTime dateTime = new DateTime(i);

            int year = dateTime.getYear();
            int month = dateTime.getMonthOfYear();
            for (Archives archive : archives) {
                if (archive.getMonth() == month && archive.getYear() == year) {
                    archive.setCount(archive.getCount() + 1);
                }
            }
            archives.add(new Archives(year,month,1));
        }
        return Result.success(archives);
    }
    @Autowired
    private ThreadService threadService;
    @Override
    public ArticleVo findArticleById(long id) throws InterruptedException {
        /**
         * 1.根据id查询
         * 2.根据bodyid和categoryid去做关联查询
         */
        Article article = articleMapper.selectById(id);
        threadService.updateViewCount(articleMapper,article);
        // 查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时写加锁，阻塞其他的读操作，性能就会下降
        // 更新 增加了此次接口的耗时，如果一切更新出问题，不能影响，查看文章的操作
        // 线程池
        // 可以把更新操作吗，可以把更新操作扔到线程池中去执行，和主线程就不想关了

        return copy(article, true, true, true, true);
    }

    @Override
    public Result PostArticle(@RequestBody PostArticleParm postArticleParm) {
        Article article = new Article();
        article.setAuthorId(postArticleParm.getId());
        article.setCategoryId(postArticleParm.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(postArticleParm.getSummary());
        article.setTitle(postArticleParm.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);
        //tags
        List<TagVo> list = postArticleParm.getTags();
        if (list != null) {
            for(TagVo i : list) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(i.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(postArticleParm.getBody().getContent());
        articleBody.setContentHtml(postArticleParm.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }
}
