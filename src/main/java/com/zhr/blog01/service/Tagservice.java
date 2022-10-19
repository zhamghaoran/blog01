package com.zhr.blog01.service;

import com.zhr.blog01.vo.params.Result;
import com.zhr.blog01.vo.params.TagVo;

import java.util.List;

public interface Tagservice {
    List<TagVo> findTagsbyArticleId(Long articled);

    Result hots(int limit);

    Result getAllTags();

    Result getDetail();
}
