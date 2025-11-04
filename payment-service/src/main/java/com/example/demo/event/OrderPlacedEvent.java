package com.example.demo.event;

import java.util.List;

public class OrderPlacedEvent {
    private Long orderId;
    private Long userId;
    private Double totalAmount;
    private List<OrderItem> items;
    private String status;
    // inner static class for item details
    public static class OrderItem {
        private int menuItemId;
        private int quantity;
        
        // getters/setters
        public int getMenuItemId() { return menuItemId; }
        public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // getters/setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

