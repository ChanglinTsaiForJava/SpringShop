package com.shop.project.service;

import com.shop.project.model.Product;
import com.shop.project.pyaload.ProductDTO;
import com.shop.project.pyaload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts();
    ProductResponse searchByCategory(Long categoryId);
}
