package com.shop.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;


    @ManyToOne
    @JoinColumn(name = "cart_id")//@JoinColumn 是「外鍵在我這裡」表示這個實體持有外鍵欄位（FK），對應資料表上會有一個欄位像 user_id、cart_id 這種。
    private Cart cart;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;
    private double productPrice;
    private double discount;
}
