package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    @Transactional
    public Cart addToCart(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productService.getProductById(productId);

        if (!product.isAvailable()) {
            throw new RuntimeException("El producto no está disponible");
        }

        CartItem item = new CartItem(product, quantity);
        cart.addItem(item);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateQuantity(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);

        if (quantity <= 0) {
            cart.removeItem(productId);
        } else {
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeFromCart(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.clear();
        cartRepository.save(cart);
    }

    public int getCartItemCount(User user) {
        return cartRepository.findByUser(user)
                .map(Cart::getTotalItems)
                .orElse(0);
    }
}
