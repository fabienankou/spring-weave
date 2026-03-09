package com.example.springweave.payment;

public interface PaymentProvider {
    // Méthode pour déclencher le paiement chez l'opérateur (TMoney, Flooz...)
    void pay(Double amount, String currency, String phoneNumber);
    
    // Méthode pour vérifier le statut d'une transaction existante
    boolean verifyTransaction(String transactionId);
}