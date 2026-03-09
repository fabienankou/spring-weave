package com.example.springweave.payment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String internalReference;
    private String externalTransactionId;
    
    private Double amount;
    private String currency;
    private String provider;
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String rawResponse;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = TransactionStatus.PENDING; 
        }
    }

    // ==========================================
    // GETTERS ET SETTERS (Générés manuellement)
    // ==========================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInternalReference() { return internalReference; }
    public void setInternalReference(String internalReference) { this.internalReference = internalReference; }

    public String getExternalTransactionId() { return externalTransactionId; }
    public void setExternalTransactionId(String externalTransactionId) { this.externalTransactionId = externalTransactionId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public String getRawResponse() { return rawResponse; }
    public void setRawResponse(String rawResponse) { this.rawResponse = rawResponse; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}