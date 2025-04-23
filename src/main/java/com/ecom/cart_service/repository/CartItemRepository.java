package com.ecom.cart_service.repository;

import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    CartItem findByProductId(String productId);
}
