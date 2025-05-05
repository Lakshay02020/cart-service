package com.ecom.cart_service.service.impl;

import com.ecom.cart_service.dto.CartDto;
import com.ecom.cart_service.dto.CartItemDto;
import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;
import com.ecom.cart_service.feign.ProductFeignProvider;
import com.ecom.cart_service.mapper.CartItemMapper;
import com.ecom.cart_service.mapper.CartMapper;
import com.ecom.cart_service.repository.CartItemRepository;
import com.ecom.cart_service.repository.CartRepository;
import com.ecom.cart_service.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    ProductFeignProvider productFeignProvider;

    public void updateItemQuantity(String userId, String productId, int quantity) {
        Cart cart = loadCart(userId);
        log.info("Cart : {}", cart.getUserId());

        Optional<CartItem> existingItemOpt = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int updatedQuantity = existingItem.getQuantity() + quantity;

            if (updatedQuantity <= 0) {
                cart.getItems().remove(existingItem);
                cartItemRepository.delete(existingItem);
                log.info("Removed item from cart as updated quantity <= 0");
            } else {
                existingItem.setQuantity(updatedQuantity);
                cartItemRepository.save(existingItem);
                log.info("Updated quantity of existing cart item to {}", updatedQuantity);
            }
        } else {
            if (quantity > 0) {
                CartItem cartItem = new CartItem();
                cartItem.setProductId(productId);
                cartItem.setQuantity(quantity);
                Double price = productFeignProvider.getProductById(Long.valueOf(productId)).getBody().getPrice();
                log.info("Setting up price: {}" , price);
                cartItem.setPrice(price); // TODO: Replace with actual price logic if needed
                cartItem.setCart(cart);

                cartItemRepository.save(cartItem);
                cart.getItems().add(cartItem);
                log.info("New cart item added");
            } else {
                log.warn("Ignoring negative quantity for non-existent item");
            }
        }

        cartRepository.save(cart); // Optional if cascade works
    }

    @Override
    public CartDto getCartItems(String userId, Long cartId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        return  CartMapper.toDto(cart.get());
    }

    @Transactional
    @Override
    public void clearCart(String userId) {
        try {
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            cart.ifPresent(value -> cartItemRepository.deleteAllByCartId(cart.get().getId()));
        } catch (Exception e) {
            log.error("Error clearing cart for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to clear cart for userId: " + userId, e);
        }
    }

    public Cart loadCart(String userId){
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }
}