package com.shop.project.pyaload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {
    private Long cartId;
    private Double totalPrice=0.0;
    private List<ProductDTO> products= new ArrayList<>();
}
