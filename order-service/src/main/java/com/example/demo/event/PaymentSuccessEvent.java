package com.example.demo.event;


public class PaymentSuccessEvent {
    private Long orderId;
    private Long userId;
    private String message;

    public PaymentSuccessEvent() {}

    public PaymentSuccessEvent(Long orderId, Long userId, String message) {
        this.orderId = orderId;
        this.userId = userId;
        this.message = message;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }


    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
