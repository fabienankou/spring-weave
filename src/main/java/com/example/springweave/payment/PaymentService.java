package com.example.springweave.payment;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final Map<String, PaymentProvider> paymentProviders;

    // On crée le constructeur à la main, VS Code va arrêter de paniquer !
    public PaymentService(TransactionRepository transactionRepository, Map<String, PaymentProvider> paymentProviders) {
        this.transactionRepository = transactionRepository;
        this.paymentProviders = paymentProviders;
    }

    public Transaction initiatePayment(String providerName, Double amount, String currency, String phoneNumber) {
        
        PaymentProvider provider = paymentProviders.get(providerName.toLowerCase() + "Service");
        
        if (provider == null) {
            throw new IllegalArgumentException("Fournisseur de paiement non supporté : " + providerName);
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setProvider(providerName.toUpperCase());
        
        transaction = transactionRepository.save(transaction);

        provider.pay(amount, currency, phoneNumber);

        transaction.setStatus(TransactionStatus.PROCESSING);
        return transactionRepository.save(transaction);
    }
}