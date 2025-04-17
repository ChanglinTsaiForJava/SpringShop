package com.shop.project.pyaload;

import com.shop.project.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private ProductDTO product;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
