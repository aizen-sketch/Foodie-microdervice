package com.example.demo.dto;


public class OrderItemDto {
    private Long menuItemId;
    private Integer quantity;
    private String name;
    private Double price;
	public Long getMenuItemId() {
		return menuItemId;
	}
	public void setMenuItemId(Long menuItemId) {
		this.menuItemId = menuItemId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public OrderItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}

    // constructors, getters, setters
    
}