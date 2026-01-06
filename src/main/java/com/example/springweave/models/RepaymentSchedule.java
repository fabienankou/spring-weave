package com.example.springweave.models;

import com.example.springweave.models.enums.RepaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "repayment_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Lien vers le dossier parent Creditapplication

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_application_id", nullable = false)
    private CreditApplication creditApplication;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate; // Date limite de paiement

    // Montant total à payer ce mois-ci

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // capital rembourse

    @Column(name = "principal_amount", precision = 15, scale = 2)
    private BigDecimal principalAmount;

    // Part des intérêts
    @Column(name = "interest_amount", precision = 15, scale = 2)
    private BigDecimal interestAmount;

    // Frais de retard (commence à 0)
    @Column(name = "late_fee", precision = 15, scale = 2)
    private BigDecimal lateFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status = RepaymentStatus.PENDING;
}