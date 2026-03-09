package com.example.demo.controller;

import com.example.demo.model.CustomerOrder;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    // RF08 - Consultar pedidos del usuario
    @GetMapping
    public String myOrders(@AuthenticationPrincipal User user, Model model, Locale locale) {
        List<CustomerOrder> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentLocale", locale.getLanguage());
        return "orders";
    }

    // RF07 - Registrar pedido (checkout)
    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user, Model model, Locale locale) {
        var cart = cartService.getOrCreateCart(user);
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentLocale", locale.getLanguage());
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(
            @AuthenticationPrincipal User user,
            @RequestParam String shippingAddress,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes
    ) {
        try {
            CustomerOrder order = orderService.createOrderFromCart(user, shippingAddress, notes);
            redirectAttributes.addFlashAttribute("success",
                    "¡Pedido #" + order.getId() + " registrado exitosamente! Revisa tu correo para la confirmación.");
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    // RF10 - Visualizar estado del pedido
    @GetMapping("/{id}")
    public String orderDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            Model model,
            Locale locale
    ) {
        CustomerOrder order = orderService.getOrderById(id);

        // Only allow the order owner or admins to view
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentLocale", locale.getLanguage());
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        return "order-detail";
    }
}
