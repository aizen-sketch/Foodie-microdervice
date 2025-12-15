package com.example.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
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

    @PostMapping("/pay/{userId}")
    public ResponseEntity<?>  makePayment(
    		@PathVariable Long userId,
    		@RequestHeader("X-User-Id") String userIdFromToken,
            @RequestHeader("X-User-Role") String userRoleFromToken,
            @RequestBody PaymentRequest request) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
    	{
    		Payment payment = paymentService.processPayment(request);
            return ResponseEntity.ok(payment);
    	}
    	return forbiddenAccess();
    }
}

