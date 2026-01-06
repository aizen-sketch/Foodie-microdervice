package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    public boolean adminAccess( String userIdFromToken,String userRoleFromToken) {

		if("ADMIN".equalsIgnoreCase(userRoleFromToken))
		return true;
		return false;
		
	}
    
    public ResponseEntity<?> forbiddenAccess(){
		return new ResponseEntity<>("access denied",HttpStatus.FORBIDDEN); 
	}
	public boolean userAccessableWithId(
			Long userId,String userIdFromToken,String userRoleFromToken) {
		Long idcheck = Long.parseLong(userIdFromToken);
		if("USER".equalsIgnoreCase(userRoleFromToken) && idcheck==userId)
		return true;
		return false;
		
	}

    //  Place a new order (checkout)
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?>  placeOrder(@PathVariable Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(orderService.placeOrder(userId));
    	return forbiddenAccess();
    }

    //  Get all orders for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?>  getOrdersByUser(@PathVariable Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    	return forbiddenAccess();
    }

    //  Get details of a specific order
    @GetMapping("/specific/{orderId}")
    public ResponseEntity<?>  getOrderById(@PathVariable Long orderId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
    	
    	Order order = orderService.getOrderById(orderId);
    	if(userAccessableWithId(order.getUserId(),userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(order);
    	return forbiddenAccess();
    }
    // Get all orders :
    @GetMapping("/all")
    public ResponseEntity<?>  getAllOrders(
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
    	
    	List<Order> orders = orderService.getAllOrders();
    	if( adminAccess(userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(orders);
    	return forbiddenAccess();
    }
}

