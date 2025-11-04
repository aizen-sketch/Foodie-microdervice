package com.example.demo.service;
import java.util.ArrayList;
import java.util.List;
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

        // Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
        
        order.setTotalAmount(cart.getTotalAmount());

        List<OrderItem> items = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setMenuItemId(cartItem.getMenuItemId());
            MenuItemDto menuItem = getMenuItemDetails(item.getMenuItemId());
            item.setName(menuItem.getName());
            item.setPrice(menuItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setOrder(order); // attach to order
            items.add(item);
        }
        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cartService.clearCart(userId);

        // ✅ TODO Later: Publish OrderPlacedEvent → Payment & Inventory Services
        OrderPlacedEvent e=new OrderPlacedEvent();
        e.setUserId(userId);
        e.setOrderId(savedOrder.getId());
        e.setItems(items);
        e.setTotalAmount(savedOrder.getTotalAmount());
        e.setStatus("payment pending");
        orderProducer.sendOrderPlacedEvent(e);
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

