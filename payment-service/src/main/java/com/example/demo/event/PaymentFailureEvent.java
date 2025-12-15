package com.example.demo.event;

public class PaymentFailureEvent {

    private Long orderId;
    private Long userId;
    private String reason;

    public PaymentFailureEvent() {}

    public PaymentFailureEvent(Long orderId, Long userId, String reason) {
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }
}
