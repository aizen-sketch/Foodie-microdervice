package com.example.demo.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.event.OrderPlacedEvent;


@Service
public class PaymentConsumer {

    private final PaymentProducer paymentProducer;

    public PaymentConsumer(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }

    @KafkaListener(topics = "order-placed-topic", groupId = "payment-service-group")
    public void consumeOrderPlacedEvent(OrderPlacedEvent event) {
        System.out.println(" Received OrderPlacedEvent for orderId: " + event.getOrderId());
        System.out.println(" Processing payment of: " + event.getTotalAmount());

        // simulate paymentsuccess
        
        try {
            Thread.sleep(2000);
            System.out.println(" Payment successful for Order ID: " + event.getOrderId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // send PaymentSuccessEvent
        event.setStatus("pending payment");
//        paymentProducer.sendPaymentSuccessEvent(event.getOrderId(), event.getUserId());
    }
}

