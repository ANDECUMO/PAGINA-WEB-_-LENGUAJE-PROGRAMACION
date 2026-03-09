package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal User user, Model model, Locale locale) {
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentLocale", locale.getLanguage());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes
    ) {
        try {
            cartService.addToCart(user, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Producto agregado al carrito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId,
            @RequestParam int quantity,
            RedirectAttributes redirectAttributes
    ) {
        cartService.updateQuantity(user, productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Cantidad actualizada");
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId,
            RedirectAttributes redirectAttributes
    ) {
        cartService.removeFromCart(user, productId);
        redirectAttributes.addFlashAttribute("success", "Producto eliminado del carrito");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        cartService.clearCart(user);
        redirectAttributes.addFlashAttribute("success", "Carrito vaciado");
        return "redirect:/cart";
    }
}
