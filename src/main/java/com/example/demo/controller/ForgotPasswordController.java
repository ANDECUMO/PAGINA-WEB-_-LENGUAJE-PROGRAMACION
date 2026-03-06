package com.example.demo.controller;

import com.example.demo.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    private final AuthService authService;

    public ForgotPasswordController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email) {
        try {
            authService.forgotPassword(email);
            return "redirect:/forgot-password?sent";
        } catch (Exception e) {
            return "redirect:/forgot-password?error";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/reset-password?token=" + token + "&error=mismatch";
        }
        try {
            authService.resetPassword(token, password);
            return "redirect:/login?passwordReset";
        } catch (Exception e) {
            return "redirect:/reset-password?token=" + token + "&error";
        }
    }
}
