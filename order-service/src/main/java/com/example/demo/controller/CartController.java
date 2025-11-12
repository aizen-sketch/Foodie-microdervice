package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/order/cart")
public class CartController {
	
	public boolean adminAccess(
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {

		if("ADMIN".equalsIgnoreCase(userRoleFromToken))
		return true;
		return false;
		
	}
	public boolean userAccessableWithId(
			Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
		Long idcheck = Long.parseLong(userIdFromToken);
		if("USER".equalsIgnoreCase(userRoleFromToken) && idcheck==userId)
		return true;
		return false;
		
	}
	public ResponseEntity<?> forbiddenAccess(){
		return new ResponseEntity<>("access denied",HttpStatus.FORBIDDEN); 
	}

    @Autowired
    private CartService cartService;
    
    @PostMapping("/create/{userId}")
    
    public ResponseEntity<?> createCart(@PathVariable Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
	        ) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
        return ResponseEntity.ok(cartService.createCart(userId));
    	return forbiddenAccess();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
	        ) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(cartService.getCartByUser(userId));
        	return forbiddenAccess();
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<?>  addToCart(@PathVariable Long userId,
    		@RequestBody CartItem item,
    		@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
    		) {
        if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(cartService.addItemToCart(userId, item));
        	return forbiddenAccess();
    }

    @DeleteMapping("/{userId}/remove/{menuItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long userId,
    		@PathVariable Long menuItemId,
    		@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
	        ) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken))
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, menuItemId));
    	return forbiddenAccess();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
	        ) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken))
        return ResponseEntity.ok(cartService.clearCart(userId));
    	return forbiddenAccess();
    }
}
