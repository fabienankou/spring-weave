package com.example.springweave.controllers;

import com.example.springweave.models.Product;
import com.example.springweave.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Endpoint Public : Consultation catalogue
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        // Logique de récupération
        return null;
    }

    // Endpoint Vendeur : Ajout de produit (Sécurisé par API Key dans l'étape 2)
    @PostMapping("/vendor")
    public Product addProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
}