package com.ecom.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private String userId; // Identifier for the user associated with the cart
    private List<CartItemDto> cartItems;
}