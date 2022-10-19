package com.zhr.blog01.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zhr.blog01.dao.mapper.ArticleMapper;
import com.zhr.blog01.dao.mapper.CategoryMapper;
import com.zhr.blog01.dao.mapper.TagMapper;
import com.zhr.blog01.dao.pojo.Article;
import com.zhr.blog01.dao.pojo.Category;
import com.zhr.blog01.dao.pojo.Tag;
import com.zhr.blog01.service.Tagservice;
import com.zhr.blog01.vo.params.Result;
import com.zhr.blog01.vo.params.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class TagServiceImpl implements Tagservice {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
    @Override
    public List<TagVo> findTagsbyArticleId(Long articled) {
        // mybatisPlus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articled);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        /**
         * 标签所拥有的文章数量最多就是最热标签
         * 查询根据tag_id进行分组计数，从大到小排列，取前limit个
         */
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }
        //需求的是TagId 和 tagName Tag对象
        // select * from tag where id in (1,2,3,4)
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);

    }

    @Override
    public Result getAllTags() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag i : tags) {
            tagVos.add(new TagVo(i.getId(),i.getTagName(),null));
        }
        return Result.success(tagVos);
    }

    @Override
    public Result getDetail() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        List<TagVo> tagVos = new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        for (int i = 0;i < tags.size();i ++) {
            tagVos.add(new TagVo(tags.get(i).getId(),tags.get(i).getTagName(),categories.get(i).getAvatar()));
        }
        return Result.success(tagVos);
    }
}
