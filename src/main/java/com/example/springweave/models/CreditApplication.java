package com.example.springweave.models;

import com.example.springweave.models.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.Data; // les getters/setters automatiques
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit_applications")
@Data // Lombok va génèrer les getters/setters/toString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreditApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // Montant qui  demandé

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate; // exemple: 12.50 %

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths; // exemple: 3, 6, 12 mois

    @Column(name = "monthly_payment", precision = 15, scale = 2)
    private BigDecimal monthlyPayment;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CreditStatus status = CreditStatus.PENDING;

    // "mappedBy" fait référence au champ "creditApplication" dans l'enfant

    @OneToMany(mappedBy = "creditApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepaymentSchedule> repaymentSchedules;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
