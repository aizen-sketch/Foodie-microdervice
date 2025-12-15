package com.example.demo.dto;

import java.util.List;

public class OrderResponse {
    private Long orderId;
    private Long userId;
    private List<OrderItemDto> items;
    private Double totalAmount;  // NEW FIELD

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

    public List<OrderItemDto> getItems() {
        return items;
    }
    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
