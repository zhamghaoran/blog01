package com.zhr.blog01.service;

import com.zhr.blog01.vo.params.TagVo;

import java.util.List;

public interface Tagservice {
    List<TagVo> findTagsbyArticleId(Long articled);
}
