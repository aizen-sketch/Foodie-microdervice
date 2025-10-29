package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping
	public List<MenuItemDto> getAllMenuItems() {
	    return menuService.getAllItems();
	}
	@GetMapping("/{id}")
	public MenuItemDto getAllMenuById(@PathVariable Integer id) {
	     menuItems item = repo.findById(id).orElse(null) ;
		 return new MenuItemDto(item.getMenuId(), item.getName(), item.getPrice());
	}
	@PostMapping
	public MenuItemDto createMenuItem(@RequestBody MenuItemDto item) {
	    return menuService.addItem(item);
	}

	@PutMapping("/{id}")
	public MenuItemDto updateMenuItem(@PathVariable Integer id, @RequestBody MenuItemDto item) {
	    return menuService.updateItem(id, item);
	}

	@DeleteMapping("/{id}")
	public String deleteMenuItem(@PathVariable Integer id) {
	    return menuService.deleteItem(id);
	}

}
