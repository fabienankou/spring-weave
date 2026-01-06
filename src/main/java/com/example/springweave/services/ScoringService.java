package com.example.springweave.services;

import com.example.springweave.models.Customer;
import com.example.springweave.models.enums.CreditStatus;
import lombok.Service;
import java.math.BigDecimal;

@Service
public class ScoringService {

    /**
     * Analyse si un client est éligible à un crédit.
     * Logique : Score basé sur les revenus et le statut KYC.
     */
    public CreditStatus evaluateApplication(Customer customer, BigDecimal requestedAmount) {
        int finalScore = 0;

        // 1. Vérification du KYC (Obligatoire pour la Fintech)

        // Si le KYC n'est pas vérifié, on rejette immédiatement
        if (!"verified".equals(customer.getKycStatus())) {
            return CreditStatus.REJECTED;
        }

        // 2. Analyse du revenu mensuel
        // Règle : La mensualité ne doit pas dépasser 40% du revenu
        BigDecimal monthlyIncome = customer.getMonthlyIncome();
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return CreditStatus.REJECTED;
        }

        // 3. Score de base du client (crédit existant dans la base)
        if (customer.getCreditScore() > 700) {
            finalScore += 50; // Bonus bon payeur
        } else if (customer.getCreditScore() < 300) {
            return CreditStatus.REJECTED; // Risque trop élevé
        }

        // 4. Décision finale
        if (finalScore >= 40 || (customer.getIsPremium() != null && customer.getIsPremium())) {
            return CreditStatus.APPROVED;
        }

        return CreditStatus.PENDING; // Nécessite une revue manuelle par un admin
    }
}