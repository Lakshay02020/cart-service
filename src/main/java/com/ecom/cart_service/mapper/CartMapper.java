package com.ecom.cart_service.mapper;

import com.ecom.cart_service.dto.CartDto;
import com.ecom.cart_service.dto.CartItemDto;
import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartMapper {

    public static Cart toEntity(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setUserId(cartDto.getUserId());
        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemDto cartItemDto : cartDto.getCartItems()) {
            CartItemMapper.toEntity(cartItemDto, cart);
        }

        cart.setItems(cartItems);
        return cart;
    }

    public static CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        return cartDto;
    }
}

