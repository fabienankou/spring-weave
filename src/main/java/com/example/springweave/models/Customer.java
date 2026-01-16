package com.example.springweave.models;

import com.example.springweave.models.enums.KycStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter @Setter
@NoArgsConstructor
public class Customer extends AbstractUserAccount {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status")
    private KycStatus kycStatus = KycStatus.PENDING;

    // Données Financières
    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(name = "available_credit")
    private BigDecimal availableCredit = BigDecimal.ZERO;

    @Column(name = "is_premium")
    private boolean isPremium;

    // situation Démographique
    private String country;
    private String city;
    private String address;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    //Relations
    // Note: Order sera créé à la prochaine étape, je commente pour l'instant pour éviter les erreurs de compilation
    // @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    // private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<KycDocument> kycDocuments = new ArrayList<>();
}