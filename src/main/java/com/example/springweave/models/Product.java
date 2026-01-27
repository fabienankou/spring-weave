package com.example.springweave.models;

import com.example.springweave.models.enums.ProductCondition;
import com.example.springweave.models.enums.StockStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "products")
public class Product extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;
    private String subcategory;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 3)
    private String currency = "XOF";

    @Enumerated(EnumType.STRING)
    // ATTENTION : "condition" est un mot réservé en SQL (MySQL, etc.).
    // Il est plus sûr d'utiliser "product_condition" comme nom de colonne.
    @Column(name = "product_condition")
    private ProductCondition condition = ProductCondition.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status")
    private StockStatus stockStatus = StockStatus.IN_STOCK;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    // --- Spécificités Fintech ---
    @Column(name = "is_credit_eligible")
    private boolean isCreditEligible = true;

    @Column(name = "min_credit_duration")
    private Integer minCreditDuration = 3;

    @Column(name = "views_count")
    private int viewsCount = 0;

    @Column(name = "average_rating")
    private double averageRating = 0.0;

    // --- Données Complexes (Compatible H2 & Postgres/MySQL) ---

    // 1. Gestion des Images (Liste de Strings)
    // @ElementCollection crée automatiquement une table secondaire 'product_images'.
    // Cela fonctionne parfaitement avec H2.
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    // 2. Gestion des Spécifications (Map)
    // J'ai changé Object en String. JPA gère mal le type "Object" générique en base de données
    // sans convertisseur JSON complexe. Map<String, String> est natif et simple.
    @ElementCollection
    @CollectionTable(name = "product_specifications", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value")
    private Map<String, String> specifications = new HashMap<>();

    // --- Getters et Setters ---

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ProductCondition getCondition() {
        return condition;
    }

    public void setCondition(ProductCondition condition) {
        this.condition = condition;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    // Convention Java : pour un booléen, le getter est "isAvailable", pas "getIsAvailable"
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isCreditEligible() {
        return isCreditEligible;
    }

    public void setCreditEligible(boolean creditEligible) {
        isCreditEligible = creditEligible;
    }

    public Integer getMinCreditDuration() {
        return minCreditDuration;
    }

    public void setMinCreditDuration(Integer minCreditDuration) {
        this.minCreditDuration = minCreditDuration;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    // Notez le changement de type ici (String au lieu de Object)
    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}