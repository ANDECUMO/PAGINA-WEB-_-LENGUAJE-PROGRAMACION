package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String products(
            @RequestParam(required = false) Long category,
            Model model, 
            Principal principal,
            Locale locale
    ) {
        List<Product> products;
        
        if (category != null) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAvailableProducts();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        model.addAttribute("currentLocale", locale.getLanguage());
        return "products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model, Principal principal, Locale locale) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("username", principal != null ? principal.getName() : "invitado");
            model.addAttribute("currentLocale", locale.getLanguage());
            return "product-detail";
        } catch (Exception e) {
            return "redirect:/products?error=not-found";
        }
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false, defaultValue = "") String keyword,
            Model model,
            Principal principal,
            Locale locale
    ) {
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("username", principal != null ? principal.getName() : "invitado");
        model.addAttribute("currentLocale", locale.getLanguage());
        return "products";
    }
}
