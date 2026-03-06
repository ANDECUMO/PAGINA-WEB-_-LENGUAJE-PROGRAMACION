package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String profile(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            model.addAttribute("user", user);
            model.addAttribute("username", principal.getName());
        }
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            model.addAttribute("user", user);
            model.addAttribute("username", principal.getName());
        }
        return "profile-edit";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email,
            Principal principal
    ) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
            
            return "redirect:/profile?success";
        }
        return "redirect:/login";
    }
}
