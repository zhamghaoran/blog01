package com.zhr.blog01.service;

import com.zhr.blog01.vo.params.CategoryVo;
import org.springframework.stereotype.Service;

public interface CategoryService {
    CategoryVo findCategoryById(Long id);
}
