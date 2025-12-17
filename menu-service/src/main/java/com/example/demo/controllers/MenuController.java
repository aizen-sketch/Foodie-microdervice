package com.example.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	        ) {
	    return ResponseEntity.ok(menuService.getAllItems());
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getAllMenuById(@PathVariable Integer id) {
	     menuItems item = repo.findById(id).orElse(null) ;
	     return ResponseEntity.ok(new MenuItemDto(item.getMenuId(), item.getName(), item.getPrice()));
	}
	@PostMapping
	public ResponseEntity<?>  createMenuItem(@RequestBody MenuItemDto item,
	        @RequestParam MultipartFile image,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
			) {
		if(adminAccess(userIdFromToken, userRoleFromToken)==true)
	    return ResponseEntity.ok(menuService.addItem(item));
		return forbiddenAccess();
	}

	
	@PostMapping("update/{id}")
	public ResponseEntity<?> updateMenuItem(@PathVariable Integer id,
			@RequestPart("menu") menuItems menu,
	        @RequestPart("image") MultipartFile image,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
			) throws IOException {
		if(adminAccess(userIdFromToken, userRoleFromToken)==true)
	    return ResponseEntity.ok(menuService.updateItem(id, menu,image));
		return forbiddenAccess();
	}
	
	@PostMapping("/addImage/{id}")
	public ResponseEntity<?> addImage(@PathVariable Integer id,
	        @RequestPart("image") MultipartFile image,
			@RequestHeader("X-User-Id") String userIdFromToken,
	        @RequestHeader("X-User-Role") String userRoleFromToken
			) throws IOException {
		if(adminAccess(userIdFromToken, userRoleFromToken)==true)
	    return ResponseEntity.ok(menuService.updateItem(id,image));
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
	@GetMapping("/topThree")
	public ResponseEntity<?> getTopMenuItems() {
	    return ResponseEntity.ok(menuService.getTopThreeItem());
	}
	
	@PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addMenuItem(
	        @RequestPart("menu") menuItems menu,
	        @RequestPart("image") MultipartFile image) throws IOException {
		System.out.println(menu.getName());
		System.out.println(image.getOriginalFilename());
		MenuItemDto saved =menuService.addItemWithImage( menu, image);
	    return ResponseEntity.ok(saved);
	}
	
	
	
	@GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) throws RuntimeException {

        menuItems existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or IMAGE_PNG
                .body(existing.getImage());
    }

}
