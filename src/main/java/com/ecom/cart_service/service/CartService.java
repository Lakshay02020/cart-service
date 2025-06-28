package com.ecom.cart_service.service;

import com.ecom.cart_service.dto.CartDto;
import com.ecom.cart_service.dto.CartItemDto;

import java.util.List;

public interface CartService {
    void updateItemQuantity(String userId, String productId, int quantity);
    CartDto getCartItems(String userId);
    void clearCart(String userId);
}
