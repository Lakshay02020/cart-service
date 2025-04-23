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
        if (cartItemDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();

        if (cartId != null) {
            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            if (cartOptional.isEmpty()) {
                throw new IllegalArgumentException("Cart not found with ID: " + cartId);
            }
            cart = cartOptional.get();
            cartItems = cart.getItems();
        } else {
            cart.setUserId(userId);
        }

        boolean itemExists = false;

        for (CartItem item : cartItems) {
            if (item.getProductId().equals(cartItemDto.getProductId())) {
                item.setQuantity(item.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            CartItem newItem = CartItemMapper.toEntity(cartItemDto, cart);
            cartItems.add(newItem);
        }

        cart.setItems(cartItems);
        cartRepository.save(cart); // saves both cart and items due to cascade
    }

    @Override
    public CartDto getCartItems(String userId, Long cartId) {
        Cart cart = cartRepository.findByUserId(userId);
        return  CartMapper.toDto(cart);
    }

}