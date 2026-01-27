package com.example.springweave.controllers;

import com.example.springweave.dtos.LoginRequest;
import com.example.springweave.dtos.RegisterRequest;
import com.example.springweave.dtos.CustomerResponse;
import com.example.springweave.models.Customer;
import com.example.springweave.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = authService.loginCustomer(request.email(), request.password());
        return ResponseEntity.ok(Map.of("token", token, "message", "Connexion réussie"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Customer customer = authService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "message", "Client enregistré avec succès",
                    "id", customer.getId(),
                    "email", customer.getEmail()
                ));
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getCurrentUser(@RequestHeader("Authorization") String token) {
        CustomerResponse customer = authService.getCurrentUserInfo(token);
        return ResponseEntity.ok(customer);
    }
}
