package com.example.springweave.services;

import com.example.springweave.models.*;
import com.example.springweave.models.enums.*;
import com.example.springweave.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final CreditService creditService;

    @Transactional
    public Transaction processPayment(Order order, PaymentGateway gateway) {
        // 1. Créer la transaction en attente
        Transaction tx = new Transaction();
        tx.setCustomer(order.getCustomer());
        tx.setOrder(order);
        tx.setAmount(order.getTotalAmount());
        tx.setType(TransactionType.PAYMENT);
        tx.setGateway(gateway);
        tx.setTransactionNumber("TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // 2. Simuler l'appel à l'API (T-Money/Flooz)
        boolean isSuccess = callExternalGateway(gateway, tx.getAmount());

        if (isSuccess) {
            tx.setStatus(TransactionStatus.COMPLETED);
            tx.setGatewayReference("EXT-REF-123");

            // 3. Mettre à jour la commande associée
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            tx.setStatus(TransactionStatus.FAILED);
        }

        return transactionRepository.save(tx);
    }

    private boolean callExternalGateway(PaymentGateway gateway, java.math.BigDecimal amount) {
        // Logique d'intégration réelle (Webhooks/REST)
        return true;
    }
}