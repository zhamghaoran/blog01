package com.zhr.blog01.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhr.blog01.dao.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询标签列表
     * @param articled
     * @return
     */
    List<Tag> findTagsByArticleId(Long articled);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(@Param("tagIds") List<Long> tagIds);
}
