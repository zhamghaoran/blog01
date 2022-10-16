package com.zhr.blog01.service.Impl;

import com.zhr.blog01.dao.mapper.CategoryMapper;
import com.zhr.blog01.dao.pojo.Category;
import com.zhr.blog01.service.CategoryService;
import com.zhr.blog01.vo.params.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public CategoryVo findCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
