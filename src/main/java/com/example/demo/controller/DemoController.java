package com.example.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "public";
    }

    @GetMapping("/user")
    public String userEndpoint(Model model, Principal principal) {
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        return "user";
    }

    @GetMapping("/admin")
    public String adminEndpoint(Model model, Principal principal) {
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        return "admin";
    }
}
