package com.example.springweave.controllers;

import com.example.springweave.models.KycDocument;
import com.example.springweave.models.enums.KycStatus;
import com.example.springweave.services.KycService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {
    private final KycService kycService;

    // Seul un Admin peut changer le statut
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<KycDocument> updateStatus(
            @PathVariable UUID id,
            @RequestParam KycStatus status,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(kycService.updateStatus(id, status, reason));
    }
}