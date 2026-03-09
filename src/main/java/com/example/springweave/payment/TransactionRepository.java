package com.example.springweave.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Cette ligne magique va dire à Spring de créer tout seul la requête SQL :
    // SELECT * FROM transaction WHERE external_transaction_id = ?
    Optional<Transaction> findByExternalTransactionId(String externalTransactionId);
}