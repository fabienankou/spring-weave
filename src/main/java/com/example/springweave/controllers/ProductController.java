package com.example.springweave.controllers;

import com.example.springweave.models.Product;
import com.example.springweave.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Endpoint Public : Consultation catalogue - Récupérer un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Endpoint Public : Récupérer tous les produits
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Endpoint Vendeur : Ajout de produit
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // Endpoint Vendeur : Mise à jour d'un produit
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    // Endpoint Vendeur : Suppression d'un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint Vendeur : Mettre à jour le stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable UUID id, @RequestParam boolean available) {
        productService.updateStock(id, available);
        return ResponseEntity.ok().build();
    }
}