package com.example.springweave.payment;

public enum TransactionStatus {
    PENDING,    // En attente
    PROCESSING, // En cours de traitement par TMoney/Flooz
    SUCCESS,    // Payé avec succès
    FAILED      // Échec ou refusé
}