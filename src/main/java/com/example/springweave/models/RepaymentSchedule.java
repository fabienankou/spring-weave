package com.example.springweave.models;

import com.example.springweave.models.enums.RepaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedules")
@Getter @Setter
public class RepaymentSchedule extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_application_id", nullable = false)
    private CreditApplication creditApplication;

    @Column(name = "installment_number")
    private Integer installmentNumber; // 1, 2, 3...

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "amount_due")
    private BigDecimal amountDue;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status = RepaymentStatus.PENDING;
}