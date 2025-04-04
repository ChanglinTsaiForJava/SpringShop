package com.shop.project.service;


import com.shop.project.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    void createCategory(Category category);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}
