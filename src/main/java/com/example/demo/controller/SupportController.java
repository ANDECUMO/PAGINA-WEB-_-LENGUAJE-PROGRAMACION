package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/support")
public class SupportController {

    @GetMapping
    public String support(Model model, Principal principal) {
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        return "support";
    }

    @GetMapping("/new")
    public String newTicket(Model model, Principal principal) {
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        return "support-new";
    }

    @GetMapping("/{id}")
    public String ticketDetail(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("ticketId", id);
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        return "support-detail";
    }
}
