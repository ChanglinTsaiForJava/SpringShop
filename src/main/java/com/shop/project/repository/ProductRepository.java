package com.shop.project.repository;

import com.shop.project.model.Category;
import com.shop.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //一如往常 repository 的語法偵測 findBy 物件內部的屬性 過濾出來後用過第一階段的物件的指定屬性排列
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);

}
