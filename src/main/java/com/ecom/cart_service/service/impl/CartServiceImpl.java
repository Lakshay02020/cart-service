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

    @Override
    public void addItem(String userId, Long cartId, CartItemDto cartItemDto) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty.");
        }

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }

        List<CartItem> items = cart.getItems();
        addItem(items, cartItemDto, cart);
        cartRepository.save(cart); // saves everything due to CascadeType.ALL
    }


    @Override
    public CartDto getCartItems(String userId, Long cartId) {
        Cart cart = cartRepository.findByUserId(userId);
        return  CartMapper.toDto(cart);
    }

    public void addItem(List<CartItem> items, CartItemDto cartItemDto, Cart cart){
        boolean itemExists = false;

        for (CartItem item : items) {
            if (item.getProductId().equals(cartItemDto.getProductId())) {
                item.setQuantity(item.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            CartItem newItem = CartItemMapper.toEntity(cartItemDto, cart);
            items.add(newItem);
        }
    }
}