package com.example.demo.event;

import java.util.List;

import com.example.demo.dto.OrderItemDto;
import com.example.demo.model.OrderItem;

public class OrderPlacedEvent {
    private Long orderId;
    private Long userId;
    private Double totalAmount;
    private List<OrderItem> items;
    private String status;
    
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
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OrderPlacedEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

    // constructors, getters, setters
    
}
