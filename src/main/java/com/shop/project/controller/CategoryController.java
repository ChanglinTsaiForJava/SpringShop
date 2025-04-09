package com.shop.project.controller;


import com.shop.project.config.AppConstant;
import com.shop.project.model.Category;
import com.shop.project.pyaload.CategoryDTO;
import com.shop.project.pyaload.CategoryResponse;
import com.shop.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


////HTTP 元件	Spring 對應的寫法
////方法（POST/GET）	@PostMapping、@GetMapping
////查詢參數 query	@RequestParam
////路徑參數 path	@PathVariable
////標頭 headers	@RequestHeader
////主體 body（JSON）	@RequestBody
////整包都要	RequestEntity<T>

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(
            @RequestParam(name="message", required = false,
                    defaultValue = "this is a default message") String message){
        return new ResponseEntity<>("ECHO MESSAGE "+ message , HttpStatus.OK);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize ){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize);
        //ResponseEntity：是一個泛型類別，用來表示完整的 HTTP 回應，包括：
        //回傳的資料（Body）
        //HTTP 狀態碼（Status）
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    //@RequestBody 幾乎都是跟 POST method 一起用的，因為「只有 POST、PUT、PATCH」
    //這類 HTTP 方法會有 request body（也就是內容資料）可以送過來。
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }


    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 @PathVariable Long categoryId){
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}
