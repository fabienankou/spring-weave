package com.example.springweave.models;

import com.example.springweave.models.enums.VendorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Note: Vendor n'hérite PAS de AbstractUserAccount car il n'a pas de password_hash dans votre SQL
@Entity
@Table(name = "vendors")
@Getter @Setter
public class Vendor extends AbstractBaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "api_key", unique = true)
    private String apiKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VendorType type;

    @Column(name = "vendor_score")
    private Integer vendorScore;

    private String country;

    @Column(name = "is_certified")
    private boolean isCertified;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}