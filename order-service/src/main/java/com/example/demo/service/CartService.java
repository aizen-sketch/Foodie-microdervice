package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.example.demo.Repository.CartRepository;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    public Double calculateTotalAmount(Cart cart) {
        double total = 0.0;

        for (CartItem item : cart.getItems()) {
            MenuItemDto menuItem = getMenuItemDetails(item.getMenuItemId());
            if (menuItem != null && menuItem.getPrice() != 0.0) {
                total += menuItem.getPrice() * item.getQuantity();
            }
        }
        return total;
        }
    
    public Cart getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }
    private MenuItemDto getMenuItemDetails(Long id) {
        String url = "http://localhost:8080/menu/" + id;
        return restTemplate.getForObject(url, MenuItemDto.class);
    }
    public Cart createCart(Long userId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if(existingCart.isPresent()) {
            return existingCart.get(); // cart already exists, return same
        }

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cart.setTotalAmount(0.0);

        return cartRepository.save(cart);
    }


    public Cart addItemToCart(Long userId, CartItem item) {
        Cart cart = getCartByUser(userId);

        boolean itemExists = false;

        for (CartItem ci : cart.getItems()) {
            if (ci.getMenuItemId().equals(item.getMenuItemId())) {
                ci.setQuantity(ci.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }
        item.setCart(cart);
        //adding item :
        if (!itemExists) {
        	cart.getItems().add(item);
            double totalBillamount = calculateTotalAmount(cart);
            cart.setTotalAmount(totalBillamount);
        }
        
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long userId, Long menuItemId) {
        Cart cart = getCartByUser(userId);

        cart.getItems().removeIf(item -> item.getMenuItemId().equals(menuItemId));
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = getCartByUser(userId);
        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        return cartRepository.save(cart);
    }

}

