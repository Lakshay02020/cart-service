package com.ecom.cart_service.service.impl;

import com.ecom.cart_service.dto.CartDto;
import com.ecom.cart_service.dto.CartItemDto;
import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;
import com.ecom.cart_service.mapper.CartItemMapper;
import com.ecom.cart_service.mapper.CartMapper;
import com.ecom.cart_service.repository.CartItemRepository;
import com.ecom.cart_service.repository.CartRepository;
import com.ecom.cart_service.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public void addItem(String userId, CartItemDto cartItemDto) {
        // 1. Fetch or create a cart for the user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        log.info("Cart : {} ", cart.getUserId());
        // 2. Create CartItem (ignore incoming ID)
        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemDto.getProductId());
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setPrice(cartItemDto.getPrice());
        cartItem.setCart(cart); // Link cart

        log.info("Cart Item Set");
        // 3. Save CartItem and update cart
        cartItemRepository.save(cartItem);
        cart.getItems().add(cartItem); // optional
        cartRepository.save(cart);     // optional if cascade works
    }

    @Override
    public CartDto getCartItems(String userId, Long cartId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        return  CartMapper.toDto(cart.get());
    }

}