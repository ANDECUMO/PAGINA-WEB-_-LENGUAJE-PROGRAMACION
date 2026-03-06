package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(unique = true, nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "name_en")
    private String nameEn;

    @Size(max = 100)
    @Column(name = "name_pt")
    private String namePt;

    @NotBlank
    @Size(max = 150)
    @Column(unique = true, nullable = false)
    private String slug;

    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 500)
    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;

    @Size(max = 500)
    @Column(name = "description_pt", columnDefinition = "TEXT")
    private String descriptionPt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // Constructors
    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.slug = generateSlug(name);
    }

    // Generate slug from name
    @PrePersist
    @PreUpdate
    private void generateSlugIfNeeded() {
        if (this.slug == null || this.slug.isEmpty()) {
            this.slug = generateSlug(this.name);
        }
    }

    private String generateSlug(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNamePt() {
        return namePt;
    }

    public void setNamePt(String namePt) {
        this.namePt = namePt;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionPt() {
        return descriptionPt;
    }

    public void setDescriptionPt(String descriptionPt) {
        this.descriptionPt = descriptionPt;
    }

    // Helper methods for i18n
    public String getLocalizedName(String locale) {
        if (locale == null) return name;
        return switch (locale.toLowerCase()) {
            case "en" -> nameEn != null ? nameEn : name;
            case "pt" -> namePt != null ? namePt : name;
            default -> name;
        };
    }

    public String getLocalizedDescription(String locale) {
        if (locale == null) return description;
        return switch (locale.toLowerCase()) {
            case "en" -> descriptionEn != null ? descriptionEn : description;
            case "pt" -> descriptionPt != null ? descriptionPt : description;
            default -> description;
        };
    }
}
