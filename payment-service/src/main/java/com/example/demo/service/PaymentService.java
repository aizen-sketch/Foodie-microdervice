package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;
	private RestTemplate restTemplate;
    
    

    public PaymentService(PaymentRepository paymentRepository, PaymentProducer paymentProducer,
    		RestTemplate restTemplate) {
    	
    	
        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
        this.restTemplate = restTemplate;
    }

    public Payment processPayment(PaymentRequest request) {
    	String orderServiceUrl = "http://localhost:8081/orders/user/" + request.getUserId();
    	try {
            // ✅ Step 1: Verify order exists in Order Service
            ResponseEntity<Object> response = restTemplate.getForEntity(orderServiceUrl, Object.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("❌ Order not found for ID: " + request.getOrderId());
            }

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException(" Order not found for ID: " + request.getOrderId());
        } catch (Exception e) {
            throw new RuntimeException(" Failed to verify order with Order Service: " + e.getMessage());
        }
    	
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setCardNumber(request.getCardNumber());
        payment.setCardHolderName(request.getCardHolderName());
        payment.setExpiryDate(request.getExpiryDate());
        payment.setBillingAddress(request.getBillingAddress());
        payment.setStatus("SUCCESS");

        paymentRepository.save(payment);

        paymentProducer.sendPaymentSuccessEvent(request.getOrderId(), request.getUserId());

        return payment;
    }
}
