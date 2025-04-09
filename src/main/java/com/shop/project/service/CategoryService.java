package com.shop.project.service;


import com.shop.project.model.Category;
import com.shop.project.pyaload.CategoryDTO;
import com.shop.project.pyaload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
