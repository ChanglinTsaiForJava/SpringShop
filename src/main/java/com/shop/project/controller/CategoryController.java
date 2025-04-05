package com.shop.project.controller;


import com.shop.project.model.Category;
import com.shop.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    //Fetch all category
    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
//        鏈式語法是靜態 不能new
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Successful");
    }
    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try{
            String status = categoryService.deleteCategory(categoryId);
            //「把 status 這段文字當成回應內容，然後回傳 HTTP 狀態碼 200 OK。」
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
//            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }


    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        try {
            Category savedCategory = categoryService.updateCategory(category,categoryId);
            return ResponseEntity.status(HttpStatus.OK).body("Category with id " + categoryId + " has been updated");
        } catch (ResponseStatusException e){
                return ResponseEntity.status(e.getStatusCode()).body(e.getReason());

        }
    }


}
