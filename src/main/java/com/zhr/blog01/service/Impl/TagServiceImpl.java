package com.zhr.blog01.service.Impl;

import com.zhr.blog01.dao.mapper.TagMapper;
import com.zhr.blog01.dao.pojo.Tag;
import com.zhr.blog01.service.Tagservice;
import com.zhr.blog01.vo.params.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TagServiceImpl implements Tagservice {
    @Autowired
    private TagMapper tagMapper;

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
}
