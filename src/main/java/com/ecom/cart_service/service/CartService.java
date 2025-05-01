package com.ecom.cart_service.service;

import com.ecom.cart_service.dto.CartDto;
import com.ecom.cart_service.dto.CartItemDto;

import java.util.List;

public interface CartService {
    void addItem(String userId, CartItemDto cartItemDto);
    CartDto getCartItems(String userId, Long cartId);
}
