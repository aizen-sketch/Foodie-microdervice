package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.menuItems;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.repository.menuRepository;

@Service
public class MenuItemService {@Autowired
	private menuRepository menuRepository;

	private MenuItemDto toDto(menuItems item) {
	    return new MenuItemDto(item.getMenuId(), item.getName(), item.getPrice());
	}

	private menuItems toEntity(MenuItemDto dto) {
	    return new menuItems(dto.getName(), dto.getPrice());
	}

	public List<MenuItemDto> getAllItems() {
	    return menuRepository.findAll()
	            .stream()
	            .map(this::toDto)
	            .collect(Collectors.toList());
	}

	public MenuItemDto addItem(MenuItemDto item) {
	    menuItems saved = menuRepository.save(toEntity(item));
	    return toDto(saved);
	}

	public MenuItemDto updateItem(Integer id, MenuItemDto updatedDto) {
	    menuItems existing = menuRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

	    existing.setName(updatedDto.getName());
	    existing.setPrice(updatedDto.getPrice());

	    return toDto(menuRepository.save(existing));
	}

	public String deleteItem(Integer id) {
	    menuRepository.deleteById(id);
	    return "Item Deleted Successfully";
	}
}