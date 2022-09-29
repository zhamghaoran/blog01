package com.zhr.blog01.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhr.blog01.dao.mapper.ArticleMapper;
import com.zhr.blog01.dao.pojo.Article;
import com.zhr.blog01.service.SysUserService;
import com.zhr.blog01.service.Tagservice;
import com.zhr.blog01.vo.params.ArticleVo;
import com.zhr.blog01.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService implements com.zhr.blog01.service.ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Tagservice tagservice;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询数据库表
     * @param pageParams
     * @return
     */
    @Override
    public List<ArticleVo> listArticlesPage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryChainWrapper = new LambdaQueryWrapper<>();
        // order by 创建时间
        // 是否置顶进行排序
        queryChainWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryChainWrapper);
        List<Article> records = articlePage.getRecords();
        // 能直接返回吗
        List<ArticleVo> articleVoList = copyList(records,true,true);
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor));
        }
        return articleVoList;

    }
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagservice.findTagsbyArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());

        }
        // 并不是所有的接口都需要标签
        return  articleVo;
    }
}
