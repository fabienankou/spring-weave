package com.example.springweave.models;

import com.example.springweave.models.enums.KycStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
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

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public List<KycDocument> getKycDocuments() {
        return kycDocuments;
    }

    public void setKycDocuments(List<KycDocument> kycDocuments) {
        this.kycDocuments = kycDocuments;
    }

    public void setPassword(String password) {
    }
}