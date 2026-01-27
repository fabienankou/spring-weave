package com.example.springweave.controllers;

import com.example.springweave.dtos.ProductResponse;
import com.example.springweave.models.Product;
import com.example.springweave.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendor/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
public class VendorProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(createdProduct));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getVendorProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> responses = products.stream().map(this::convertToResponse).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(convertToResponse(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(convertToResponse(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable UUID id,
            @RequestParam boolean available) {
        productService.updateStock(id, available);
        return ResponseEntity.ok().build();
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getCategory(),
            product.getPrice(),
            product.getCurrency(),
            product.isAvailable(),
            product.getViewsCount(),
            product.getAverageRating()
        );
    }
}
