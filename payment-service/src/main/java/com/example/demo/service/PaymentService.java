package com.example.demo.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.OrderResponse;
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
    	String orderServiceUrl = "http://localhost:8081/order/user/" + request.getUserId();
    	double amountTotal=0.0;
    	try {
    		HttpHeaders headers = new HttpHeaders();
    	    headers.set("X-User-Id", String.valueOf(request.getUserId()));
    	    headers.set("X-User-Role", "USER"); // or dynamic role if available
    	    
    	    HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            // ✅ Step 1: Verify order exists in Order Service
    	    ResponseEntity<List<OrderResponse>> response = restTemplate.exchange(
    	    		orderServiceUrl,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<List<OrderResponse>>() {}
    	    );
    	    System.out.println("Response JSON = " + response.getBody());
    	    List<OrderResponse> orders = response.getBody();
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("❌ Order not found for ID: " + request.getOrderId());
            }
            
            OrderResponse targetOrder =
                    orders.stream()
                            .filter(o -> o.getOrderId() != null &&
                                         o.getOrderId().equals(request.getOrderId()))
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException("Order not found: " + request.getOrderId())
                            );
            
            amountTotal = targetOrder.getTotalAmount();
            

        } catch (Exception e) {
            throw new RuntimeException(" Failed to verify order with Order Service: " + e.getMessage());
        }
    	if (request.getCardNumber() == null || request.getCardNumber().length() < 5) {
            String reason = "Invalid card number";
            paymentProducer.sendPaymentFailureEvent(request.getOrderId(), request.getUserId(), reason);
            throw new RuntimeException(reason);
        }
    	Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setCardNumber(request.getCardNumber());
        payment.setCardHolderName(request.getCardHolderName());
        payment.setExpiryDate(request.getExpiryDate());
        payment.setAmount(amountTotal);
        payment.setBillingAddress(request.getBillingAddress());
        payment.setStatus("SUCCESS");

        paymentRepository.save(payment);

        paymentProducer.sendPaymentSuccessEvent(request.getOrderId(), request.getUserId());

        return payment;
    }
}
