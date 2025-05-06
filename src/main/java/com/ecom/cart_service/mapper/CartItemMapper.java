package com.ecom.cart_service.mapper;

import com.ecom.cart_service.dto.CartItemDto;
import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;
import com.ecom.cart_service.feign.ProductFeignProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class CartItemMapper {

    @Autowired
    ProductFeignProvider productFeignProvider;

    public static CartItemDto toDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProductId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        return dto;
    }

    public static CartItem toEntity(CartItemDto dto, Cart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setId(dto.getId()); // usually set by DB, so can be skipped unless updating
        cartItem.setProductId(dto.getProductId());
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setPrice(dto.getPrice());
        cartItem.setCart(cart); // establish bidirectional link
        return cartItem;
    }
}
