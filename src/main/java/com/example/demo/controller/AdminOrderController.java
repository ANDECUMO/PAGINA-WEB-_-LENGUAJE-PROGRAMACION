package com.example.demo.controller;

import com.example.demo.model.CustomerOrder;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Admin view: all orders
    @GetMapping
    public String allOrders(
            @RequestParam(required = false) String status,
            Model model,
            Principal principal,
            Locale locale
    ) {
        List<CustomerOrder> orders;

        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status);
                orders = orderService.getOrdersByStatus(orderStatus);
            } catch (IllegalArgumentException e) {
                orders = orderService.getAllOrders();
            }
        } else {
            orders = orderService.getAllOrders();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("username", principal != null ? principal.getName() : "admin");
        model.addAttribute("currentLocale", locale.getLanguage());
        return "admin-orders";
    }

    // Admin: update order status
    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            orderService.updateOrderStatus(id, newStatus);
            redirectAttributes.addFlashAttribute("success",
                    "Estado del pedido #" + id + " actualizado a: " + newStatus.name());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error actualizando el pedido: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    // Admin: view order detail
    @GetMapping("/{id}")
    public String orderDetail(
            @PathVariable Long id,
            Model model,
            Principal principal,
            Locale locale
    ) {
        CustomerOrder order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("username", principal != null ? principal.getName() : "admin");
        model.addAttribute("currentLocale", locale.getLanguage());
        model.addAttribute("isAdmin", true);
        return "order-detail";
    }
}
