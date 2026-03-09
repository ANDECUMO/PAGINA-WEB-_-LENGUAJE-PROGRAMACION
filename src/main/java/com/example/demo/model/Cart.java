package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    public void addItem(CartItem item) {
        // Check if product already exists in cart
        for (CartItem existing : items) {
            if (existing.getProduct().getId().equals(item.getProduct().getId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void clear() {
        items.clear();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
