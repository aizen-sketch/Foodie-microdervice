package com.example.demo.event;

import java.util.List;

import com.example.demo.dto.OrderItemDto;

public class OrderPlacedEvent {
    private Long orderId;
    private Long userId;
    private Double totalAmount;
    private List<OrderItemDto> items;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<OrderItemDto> getItems() {
		return items;
	}
	public void setItems(List<OrderItemDto> items) {
		this.items = items;
	}
	public OrderPlacedEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

    // constructors, getters, setters
    
}
