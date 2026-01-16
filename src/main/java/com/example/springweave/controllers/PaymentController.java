package com.example.springweave.controllers;

import com.example.springweave.models.Transaction;
import com.example.springweave.models.enums.PaymentGateway;
import com.example.springweave.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<Transaction> checkout(@PathVariable UUID orderId, @RequestParam PaymentGateway gateway) {
        // Récupérer l'order et traiter
        return ResponseEntity.ok().build();
    }
}