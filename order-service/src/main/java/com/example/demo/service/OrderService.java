package com.example.demo.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.event.OrderPlacedEvent;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailservice;
    
    @Autowired
    private RestTemplate restTemplate;
    private final OrderProducer orderProducer;

    public OrderService(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }
    
    @Autowired
    private CartService cartService;
    
    private MenuItemDto getMenuItemDetails(Long id) {
        String url = "http://localhost:8080/menu/" + id;
        return restTemplate.getForObject(url, MenuItemDto.class);
    }
    
    public Order placeOrder(Long userId) {

        // Load Cart
        Cart cart = cartService.getCartByUser(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Unable to place order.");
        }

        Optional<Order> existingPendingOrder =
                orderRepository.findByUserIdAndStatus(userId, "PENDING");

        Order order;

        if (existingPendingOrder.isPresent()) {
            // ✔ Reuse existing PENDING order
            order = existingPendingOrder.get();

            // Remove old items but keep the same list instance
            order.getItems().clear();
        } else {
            // ✔ Create new order
            order = new Order();
            order.setUserId(userId);
            order.setStatus("PENDING");
        }

        // Calculate total
        order.setTotalAmount(cart.getTotalAmount() * 1.05 + 40);

        // IMPORTANT: Use the helper method
        for (CartItem cartItem : cart.getItems()) {

            OrderItem item = new OrderItem();
            item.setMenuItemId(cartItem.getMenuItemId());

            MenuItemDto menuItem = getMenuItemDetails(cartItem.getMenuItemId());
            item.setName(menuItem.getName());
            item.setPrice(menuItem.getPrice());
            item.setQuantity(cartItem.getQuantity());

            order.addItem(item);   // ✔ Attach item safely
        }

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Publish event
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setUserId(userId);
        event.setOrderId(savedOrder.getId());
        event.setItems(savedOrder.getItems()); // ✔ Use saved items
        event.setTotalAmount(savedOrder.getTotalAmount());
        event.setStatus("PENDING");

        orderProducer.sendOrderPlacedEvent(event);
//        String email = "joyitab82@gmail.com";
//        long id =1;
//        emailservice.sendPaymentSuccessEmail(email, id , 10000.0);
        return savedOrder;
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}

