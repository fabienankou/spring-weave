package com.example.springweave.models;

import com.example.springweave.models.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credit_applications")
@Getter @Setter
public class CreditApplication extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal amount; // Montant emprunté

    @Column(name = "interest_rate")
    private BigDecimal interestRate; // ex: 0.05 pour 5%

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "total_to_repay")
    private BigDecimal totalToRepay;

    @Enumerated(EnumType.STRING)
    private CreditStatus status = CreditStatus.PENDING;

    @OneToMany(mappedBy = "creditApplication", cascade = CascadeType.ALL)
    private List<RepaymentSchedule> repaymentSchedules = new ArrayList<>();
}