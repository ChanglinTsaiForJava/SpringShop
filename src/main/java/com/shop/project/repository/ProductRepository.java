package com.shop.project.repository;

import com.shop.project.model.Category;
import com.shop.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //一如往常 repository 的語法偵測 findBy 物件內部的屬性 過濾出來後用過第一階段的物件的指定屬性排列
    List<Product> findByCategoryOrderByPriceAsc(Category category);
}
