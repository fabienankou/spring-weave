package com.example.springweave.controllers;

import com.example.springweave.models.CreditApplication;
import com.example.springweave.models.enums.CreditStatus;
import com.example.springweave.services.CreditEngineService;
import com.example.springweave.services.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditEngineService creditEngineService;
    private final ScoringService scoringService;

    /**
     * Endpoint pour soumettre une nouvelle demande de crédit
     */
    @PostMapping("/apply")
    public ResponseEntity<?> applyForCredit(@RequestBody CreditApplication application) {

        // 1. Analyse du score de crédit via ton service du Jour 4
        CreditStatus decision = scoringService.evaluateApplication(
                application.getCustomer(),
                application.getAmount()
        );


        if (decision == CreditStatus.REJECTED) {
            return ResponseEntity.badRequest().body("Demande rejetée :  revenus insuffisants vous n etes pas eligible.");
        }

        // 3. Si approuvé ou en attente, on crée le dossier et l'échéancier (Jour 3)

        application.setStatus(decision);
        CreditApplication result = creditEngineService.createCreditWithSchedule(application);

        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour consulter l'état d'un crédit
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditApplication> getCreditDetails(@PathVariable UUID id) {
        // Ici tu pourrais appeler ton repository du Jour 2
        return ResponseEntity.ok().build();
    }
}