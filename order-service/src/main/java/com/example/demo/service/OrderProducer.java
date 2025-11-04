package com.example.demo.service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.event.OrderPlacedEvent;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderPlacedEvent(OrderPlacedEvent event) {
        kafkaTemplate.send("order-placed-topic", event);
        System.out.println(" Published OrderPlacedEvent for order: " + event.getOrderId());
    }
}

