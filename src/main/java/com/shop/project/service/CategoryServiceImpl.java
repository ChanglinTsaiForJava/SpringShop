package com.shop.project.service;

import com.shop.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//tell spring to manage it as a bean
@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories= new ArrayList<>();
    //manageId
    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategory() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        //把 categories 這個 List 轉成一個 Stream（串流）。
        //意思是：篩選出 categoryId 等於指定 categoryId 的項目。
        //c 是每一筆 category 資料，c.getCategoryId() 是它的 ID。
        //.equals(categoryId) 是拿來比對你想刪除的那個 ID。
        //所以這段會從整個 List 裡找出「ID 一樣的那一筆」。
        Category category = categories.stream().filter(c->c.getCategoryId().equals(categoryId)).findFirst()
                //new ResponseStatusException(狀態碼, 錯誤訊息)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        categories.remove(category);
        return "Category with id " + categoryId + " has been deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory = categories.stream().filter(c->c.getCategoryId().equals(categoryId)).findFirst();
        if(optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }
    }


}
