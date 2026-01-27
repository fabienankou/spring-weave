package com.example.springweave.models;

import com.example.springweave.models.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "transactions")
@Getter @Setter
public class Transaction extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repayment_schedule_id")
    private RepaymentSchedule repaymentSchedule;

    @Column(name = "transaction_number", unique = true, nullable = false)
    private String transactionNumber; // Ex: TX-2024-ABCDE

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private String currency = "XOF";

    @Enumerated(EnumType.STRING)
    private PaymentGateway gateway;

    @Column(name = "gateway_reference")
    private String gatewayReference; // ID retourné par T-Money ou Stripe

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata; // Infos brutes de l'opérateur
}