package com.zhr.blog01.service;

import com.zhr.blog01.vo.params.CategoryVo;
import com.zhr.blog01.vo.params.Result;
import org.springframework.stereotype.Service;

public interface CategoryService {
    CategoryVo findCategoryById(Long id);

    Result findAll();

    Result getDetail();

    Result categoriesById(Long id);

}
