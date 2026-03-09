package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService,
                        EmailService emailService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Transactional
    public CustomerOrder createOrderFromCart(User user, String shippingAddress, String notes) {
        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setNotes(notes);
        order.setStatus(OrderStatus.PENDIENTE);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            );
            order.addItem(orderItem);
        }
        order.calculateTotal();

        CustomerOrder savedOrder = orderRepository.save(order);

        // Clear the cart after ordering
        cartService.clearCart(user);

        // Send confirmation email to user (RF09)
        try {
            emailService.sendOrderConfirmationEmail(user.getEmail(), user.getName(), savedOrder);
        } catch (Exception e) {
            // Log but don't fail the order
            System.err.println("Error enviando email de confirmación: " + e.getMessage());
        }

        // Notify all admins about the new order
        try {
            List<User> admins = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == User.Role.ADMIN)
                    .toList();
            for (User admin : admins) {
                emailService.sendNewOrderNotificationToAdmin(admin.getEmail(), admin.getName(), savedOrder);
            }
        } catch (Exception e) {
            System.err.println("Error notificando admins: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<CustomerOrder> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public CustomerOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<CustomerOrder> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional
    public CustomerOrder updateOrderStatus(Long orderId, OrderStatus newStatus) {
        CustomerOrder order = getOrderById(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        CustomerOrder savedOrder = orderRepository.save(order);

        // Notify user about status change
        try {
            emailService.sendOrderStatusUpdateEmail(
                    order.getUser().getEmail(),
                    order.getUser().getName(),
                    savedOrder,
                    oldStatus
            );
        } catch (Exception e) {
            System.err.println("Error enviando email de actualización: " + e.getMessage());
        }

        return savedOrder;
    }
}
