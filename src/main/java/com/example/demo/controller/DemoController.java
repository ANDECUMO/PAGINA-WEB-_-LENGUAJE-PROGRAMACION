package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Ruta PUBLIC: acceso libre sin autenticación.";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "Ruta USER: accesible para USER o ADMIN.";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Ruta ADMIN: accesible solo para ADMIN.";
    }
}
