package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendPaymentSuccessEmail(String to, Long long1, Double amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Payment Successful - Order #" + long1);
        message.setText("Hi, your payment of ₹" + amount + " for order ID " + long1 + " was successful.\n\nThank you for shopping with us!");
        mailSender.send(message);
        System.out.println("✅ Email sent to: " + to);
    }
}

