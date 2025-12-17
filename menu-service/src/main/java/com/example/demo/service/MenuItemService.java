package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

	public String deleteItem(Integer id) {
	    menuRepository.deleteById(id);
	    return "Item Deleted Successfully";
	}
	public List<MenuItemDto> getTopThreeItem() {
		List<menuItems> topThreeItems = menuRepository.findAnyThree();
		List<MenuItemDto> topThreeItemsDto = new ArrayList<>();
		for(menuItems item : topThreeItems) {
			topThreeItemsDto.add(toDto(item));
		}
		return topThreeItemsDto;
		
	}

	public MenuItemDto addItemWithImage(menuItems menu, MultipartFile image) throws IOException {
		// TODO Auto-generated method stub
		menu.setImage(image.getBytes());
		menuItems saved = menuRepository.save(menu);
		return toDto(saved);
	}

	public Object updateItem(Integer id, menuItems menu, MultipartFile image) throws IOException {
		menuItems existing = menuRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

	    existing.setName(menu.getName());
	    existing.setPrice(menu.getPrice());
	    existing.setImage(image.getBytes());
	    return toDto(menuRepository.save(existing));
	}

	public Object updateItem(Integer id, MultipartFile image) throws IOException {
		// TODO Auto-generated method stub
		menuItems existing = menuRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

	    existing.setImage(image.getBytes());
	    return toDto(menuRepository.save(existing));
	}
}