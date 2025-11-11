package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Entity.menuItems;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.repository.menuRepository;
import com.example.demo.service.MenuItemService;

@RestController
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private menuRepository repo;
	@Autowired
	private MenuItemService menuService;
	
	public boolean adminAccess(
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {

		if("ADMIN".equalsIgnoreCase(userRoleFromToken))
		return true;
		return false;
		
	}
	
	public ResponseEntity<?> forbiddenAccess(){
		return new ResponseEntity<>("Only admin can access",HttpStatus.FORBIDDEN); 
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllMenuItems(
	        @RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
	        ) {
	    return ResponseEntity.ok(menuService.getAllItems());
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getAllMenuById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
	        ) {
	     menuItems item = repo.findById(id).orElse(null) ;
	     return ResponseEntity.ok(new MenuItemDto(item.getMenuId(), item.getName(), item.getPrice()));
	}
	@PostMapping
	public ResponseEntity<?>  createMenuItem(@RequestBody MenuItemDto item,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
			) {
		if(adminAccess(userIdFromToken, userRoleFromToken)==true)
	    return ResponseEntity.ok(menuService.addItem(item));
		return forbiddenAccess();
	}

	@PostMapping("update/{id}")
	public ResponseEntity<?> updateMenuItem(@PathVariable Integer id,@RequestBody MenuItemDto item,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
			) {
		if(adminAccess(userIdFromToken, userRoleFromToken)==true)
	    return ResponseEntity.ok(menuService.updateItem(id, item));
		return forbiddenAccess();
	}
	

	@DeleteMapping("/{id}")
	public ResponseEntity<?>  deleteMenuItem(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken) {
		if(adminAccess(userIdFromToken, userRoleFromToken)==true)
			return ResponseEntity.ok(menuService.deleteItem(id));
		return forbiddenAccess();
	}

}
