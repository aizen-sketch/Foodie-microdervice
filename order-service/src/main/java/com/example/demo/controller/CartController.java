package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/order/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @PostMapping("/create/{userId}")
    
    public Cart createCart(@PathVariable Long userId) {
        return cartService.createCart(userId);
    }

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @PostMapping("/{userId}/add")
    public Cart addToCart(@PathVariable Long userId, @RequestBody CartItem item) {
        return cartService.addItemToCart(userId, item);
    }

    @DeleteMapping("/{userId}/remove/{menuItemId}")
    public Cart removeFromCart(@PathVariable Long userId, @PathVariable Long menuItemId) {
        return cartService.removeItemFromCart(userId, menuItemId);
    }

    @DeleteMapping("/{userId}/clear")
    public Cart clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
}
