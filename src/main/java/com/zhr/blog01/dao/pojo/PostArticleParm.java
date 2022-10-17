package com.zhr.blog01.dao.pojo;

import com.zhr.blog01.vo.params.ArticleBodyParm;
import com.zhr.blog01.vo.params.ArticleBodyVo;
import com.zhr.blog01.vo.params.CategoryVo;
import com.zhr.blog01.vo.params.TagVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PostArticleParm {
    private String title;
    private Long id;
    private ArticleBodyParm body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;
}
