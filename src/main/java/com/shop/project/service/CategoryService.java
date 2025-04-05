package com.shop.project.service;


import com.shop.project.model.Category;
import com.shop.project.pyaload.CategoryDTO;
import com.shop.project.pyaload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}
