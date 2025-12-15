package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.details;
import com.example.demo.service.userDetailsService;

@RestController
@RequestMapping("/details")
public class detailsController {
	
	@Autowired
	userDetailsService detailService;
	
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
	
	@GetMapping("/fetch/{userId}")
    public ResponseEntity<?>  healthCheck(@PathVariable Long userId,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
            return ResponseEntity.ok(detailService.getDetailsById(userId));
    	return forbiddenAccess();
    }
	
	
	@PostMapping("/add/{userId}")
    public ResponseEntity<?>  addDetails(@PathVariable Long userId,
    		@RequestBody details userDetails,
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
		
    	if(userAccessableWithId(userId,userIdFromToken,userRoleFromToken)|| adminAccess(userIdFromToken,userRoleFromToken))
    	{
    		if(detailService.getDetailsById(userId) != null) {
    			return ResponseEntity.ok(detailService.updateDetailsById(userId,userDetails));
    		}
    		return ResponseEntity.ok(detailService.addDetails(userId,userDetails));
    	}
    		
    	return forbiddenAccess();
    }
	
	
}
