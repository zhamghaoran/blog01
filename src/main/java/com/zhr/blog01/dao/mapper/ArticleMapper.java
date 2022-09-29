package com.zhr.blog01.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhr.blog01.dao.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
