package com.example.springweave.models.enums;

public enum CreditStatus {
    PENDING,        // En attente d'analyse
    APPROVED,       // Accepté par le moteur de risque
    REJECTED,       // Refusé
    ACTIVE,         // Fonds décaissés, remboursement en cours
    COMPLETED,      // Tout remboursé
    DEFAULTED       // En défaut de paiement (mauvais payeur)
}
