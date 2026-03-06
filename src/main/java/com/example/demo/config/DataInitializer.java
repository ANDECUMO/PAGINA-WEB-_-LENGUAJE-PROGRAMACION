package com.example.demo.config;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create users if they don't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setName("Administrador");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("✓ Usuario admin creado");
        }

        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setName("Usuario Normal");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(User.Role.USER);
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("✓ Usuario user creado");
        }

        // Create categories if they don't exist
        Category hamburguesas = createCategoryIfNotExists(
                "Hamburguesas",
                "Deliciosas hamburguesas artesanales con ingredientes frescos",
                "Burgers",
                "Delicious artisan burgers with fresh ingredients",
                "Hambúrgueres",
                "Deliciosos hambúrgueres artesanais com ingredientes frescos"
        );
        
        Category bebidas = createCategoryIfNotExists(
                "Bebidas",
                "Bebidas frías y calientes para acompañar tu comida",
                "Drinks",
                "Cold and hot drinks to accompany your meal",
                "Bebidas",
                "Bebidas frias e quentes para acompanhar sua refeição"
        );
        
        Category postres = createCategoryIfNotExists(
                "Postres",
                "Dulces tentaciones para terminar tu comida",
                "Desserts",
                "Sweet temptations to finish your meal",
                "Sobremesas",
                "Doces tentações para terminar sua refeição"
        );
        
        Category entradas = createCategoryIfNotExists(
                "Entradas",
                "Perfectas para comenzar tu experiencia gastronómica",
                "Appetizers",
                "Perfect to start your gastronomic experience",
                "Entradas",
                "Perfeitas para começar sua experiência gastronômica"
        );

        // Create products if they don't exist
        if (productRepository.count() == 0) {
            // Hamburguesas
            createProduct("Hamburguesa Clásica", 
                    "Hamburguesa de carne 100% vacuna con lechuga, tomate, cebolla y queso", 
                    new BigDecimal("12.99"), 
                    "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400",
                    hamburguesas,
                    "Classic Burger",
                    "100% beef burger with lettuce, tomato, onion and cheese",
                    "Hambúrguer Clássico",
                    "Hambúrguer de carne 100% bovina com alface, tomate, cebola e queijo");
            
            createProduct("Hamburguesa BBQ", 
                    "Hamburguesa con salsa BBQ, bacon crujiente, cebolla caramelizada y queso cheddar", 
                    new BigDecimal("15.99"), 
                    "https://images.unsplash.com/photo-1550547660-d9450f859349?w=400",
                    hamburguesas,
                    "BBQ Burger",
                    "Burger with BBQ sauce, crispy bacon, caramelized onion and cheddar cheese",
                    "Hambúrguer BBQ",
                    "Hambúrguer com molho BBQ, bacon crocante, cebola caramelizada e queijo cheddar");
            
            createProduct("Hamburguesa Vegana", 
                    "Hamburguesa 100% vegetal con aguacate, lechuga y tomate", 
                    new BigDecimal("13.99"), 
                    "https://images.unsplash.com/photo-1585238342024-78d387f4a707?w=400",
                    hamburguesas,
                    "Vegan Burger",
                    "100% plant-based burger with avocado, lettuce and tomato",
                    "Hambúrguer Vegano",
                    "Hambúrguer 100% vegetal com abacate, alface e tomate");

            // Bebidas
            createProduct("Coca Cola", 
                    "Refresco de cola 500ml", 
                    new BigDecimal("2.99"), 
                    "https://images.unsplash.com/photo-1554866585-cd94860890b7?w=400",
                    bebidas,
                    "Coca Cola",
                    "Cola soft drink 500ml",
                    "Coca Cola",
                    "Refrigerante de cola 500ml");
            
            createProduct("Jugo Natural de Naranja", 
                    "Jugo de naranja recién exprimido", 
                    new BigDecimal("4.99"), 
                    "https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400",
                    bebidas,
                    "Fresh Orange Juice",
                    "Freshly squeezed orange juice",
                    "Suco Natural de Laranja",
                    "Suco de laranja espremido na hora");
            
            createProduct("Café Americano", 
                    "Café americano caliente", 
                    new BigDecimal("3.50"), 
                    "https://images.unsplash.com/photo-1511920170033-f8396924c348?w=400",
                    bebidas,
                    "Americano Coffee",
                    "Hot americano coffee",
                    "Café Americano",
                    "Café americano quente");

            // Postres
            createProduct("Brownie con Helado", 
                    "Brownie de chocolate caliente con helado de vainilla", 
                    new BigDecimal("6.99"), 
                    "https://images.unsplash.com/photo-1624353365286-3f8d62daad51?w=400",
                    postres,
                    "Brownie with Ice Cream",
                    "Hot chocolate brownie with vanilla ice cream",
                    "Brownie com Sorvete",
                    "Brownie de chocolate quente com sorvete de baunilha");
            
            createProduct("Cheesecake de Fresa", 
                    "Tarta de queso con mermelada de fresa", 
                    new BigDecimal("7.99"), 
                    "https://images.unsplash.com/photo-1533134486753-c833f0ed4866?w=400",
                    postres,
                    "Strawberry Cheesecake",
                    "Cheesecake with strawberry jam",
                    "Cheesecake de Morango",
                    "Torta de queijo com geleia de morango");

            // Entradas
            createProduct("Papas Fritas", 
                    "Papas fritas crujientes con salsa especial", 
                    new BigDecimal("4.50"), 
                    "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=400",
                    entradas,
                    "French Fries",
                    "Crispy french fries with special sauce",
                    "Batatas Fritas",
                    "Batatas fritas crocantes com molho especial");
            
            createProduct("Aros de Cebolla", 
                    "Aros de cebolla rebozados y fritos", 
                    new BigDecimal("5.50"), 
                    "https://images.unsplash.com/photo-1639024471283-03518883512d?w=400",
                    entradas,
                    "Onion Rings",
                    "Battered and fried onion rings",
                    "Anéis de Cebola",
                    "Anéis de cebola empanados e fritos");

            System.out.println("✓ Productos de ejemplo creados");
        }
    }

    private Category createCategoryIfNotExists(String name, String description, 
                                              String nameEn, String descriptionEn,
                                              String namePt, String descriptionPt) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category category = new Category(name, description);
                    category.setNameEn(nameEn);
                    category.setDescriptionEn(descriptionEn);
                    category.setNamePt(namePt);
                    category.setDescriptionPt(descriptionPt);
                    Category saved = categoryRepository.save(category);
                    System.out.println("✓ Categoría creada: " + name);
                    return saved;
                });
    }

    private void createProduct(String name, String description, BigDecimal price, 
                              String imageUrl, Category category,
                              String nameEn, String descriptionEn,
                              String namePt, String descriptionPt) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setNameEn(nameEn);
        product.setDescriptionEn(descriptionEn);
        product.setNamePt(namePt);
        product.setDescriptionPt(descriptionPt);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setAvailable(true);
        productRepository.save(product);
    }
}
