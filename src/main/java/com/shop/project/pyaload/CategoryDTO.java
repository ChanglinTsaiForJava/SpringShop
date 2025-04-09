package com.shop.project.pyaload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



//出入都會是DTO但是在運算過程把dto轉成一般類別 運算完再轉回dto顯示
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
}
