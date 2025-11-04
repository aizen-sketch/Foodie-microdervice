package com.example.demo.service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.event.PaymentSuccessEvent;

@Service
public class PaymentProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentSuccessEvent(Long orderId, Long userId) {
        PaymentSuccessEvent event = new PaymentSuccessEvent(orderId, userId, "Payment successful");
        System.out.println("📤 Sending PaymentSuccessEvent for orderId: " + orderId);
        kafkaTemplate.send("payment-success-topic", event);
    }
}
