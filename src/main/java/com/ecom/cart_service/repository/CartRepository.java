package com.ecom.cart_service.repository;

import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(String userId);
}
