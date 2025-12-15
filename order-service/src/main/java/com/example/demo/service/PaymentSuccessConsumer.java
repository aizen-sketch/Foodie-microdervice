package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.OrderRepository;
import com.example.demo.event.PaymentFailureEvent;
import com.example.demo.event.PaymentSuccessEvent;
import com.example.demo.model.Order;

@Service
public class PaymentSuccessConsumer {

    private final OrderRepository orderRepository;

    public PaymentSuccessConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Autowired
    private CartService cartService;

    @KafkaListener(topics = "payment-success-topic", groupId = "order-service-group")
    public void consumePaymentSuccessEvent(PaymentSuccessEvent event) {
        System.out.println("✅Payment success event received for orderId: " + event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId()).orElse(null);
        if (order != null) {
            order.setStatus(event.getMessage());
            orderRepository.save(order);
            cartService.clearCart(event.getUserId());
            System.out.println("💾 Order status updated to COMPLETED for ID: " + event.getOrderId());
        } else {
            System.out.println("⚠️ Order not found for ID: " + event.getOrderId());
        }
    }
    
    @KafkaListener(topics = "payment-failure-topic", groupId = "order-service")
    public void handlePaymentFailure(PaymentFailureEvent event) {
        System.out.println("❌ Payment failed for Order ID: " + event.getOrderId()
                + ", Reason: " + event.getReason());

        // Check if order exists
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            System.out.println("🗑️ Deleting order due to payment failure: " + order.getId());
            orderRepository.delete(order);
        });
    }
}
