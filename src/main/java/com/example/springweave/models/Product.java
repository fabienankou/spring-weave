package com.example.springweave.models;

import com.example.springweave.models.enums.ProductCondition;
import com.example.springweave.models.enums.StockStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter
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
    @Column(name = "condition")
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

    // --- Données Complexes (JSONB) ---
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> specifications;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> images;
}