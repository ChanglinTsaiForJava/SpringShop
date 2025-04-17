package com.shop.project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    //mappedBy 是「外鍵在對方那裡」 mappedBy 的屬性本身並不是一個欄位。
    //在 JPA 中，mappedBy 是用來指定在另一個實體類別中，哪個屬性負責維護關聯的。
    // 它並不是資料表中的一個欄位，而是一個指向其他實體屬性的引用，用來建立物件之間的關聯。
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<CartItem> cartItems= new ArrayList<>();


    private double totalPrice = 0.0;




}
