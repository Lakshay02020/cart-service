package com.ecom.cart_service.controller;

import com.ecom.cart_service.dto.CartDto;
import com.ecom.cart_service.dto.CartItemDto;
import com.ecom.cart_service.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/updateQuantity/{productId}")
    public ResponseEntity<String> updateItemQuantity(@PathVariable String userId, @PathVariable String productId, @RequestParam int quantity) {
        log.info("Received request to add item with product id {} to increase/decrease by {} for user {}", productId, quantity, userId);
        cartService.updateItemQuantity(userId, productId, quantity);
        return new ResponseEntity<String>("Item added to cart successfully", HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        log.info("Received request to fetch cart for user {}", userId);
        CartDto cartDto = cartService.getCartItems(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

//    @PutMapping("/{userId}/items/{itemId}")
//    public ResponseEntity<String> updateItemQuantity(@PathVariable String userId, @PathVariable String itemId, @RequestParam int quantity) {
//        logger.info("Received request to update quantity of item {} to {} for user {}", itemId, quantity, userId);
//        boolean updated = cartService.updateItemQuantity(userId, itemId, quantity);
//        if (updated) {
//            return new ResponseEntity<>("Cart item quantity updated successfully", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Item not found in cart", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{userId}/items/{itemId}")
//    public ResponseEntity<String> removeItem(@PathVariable String userId, @PathVariable String itemId) {
//        logger.info("Received request to remove item {} for user {}", itemId, userId);
//        boolean removed = cartService.removeItem(userId, itemId);
//        if (removed) {
//            return new ResponseEntity<>("Item removed from cart successfully", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Item not found in cart", HttpStatus.NOT_FOUND);
//        }
//    }
//
    @DeleteMapping("/clearItems/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        log.info("Received request to clear cart for user {}", userId);
        cartService.clearCart(userId);
        return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
    }
}