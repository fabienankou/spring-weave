package com.example.springweave.services;

import com.example.springweave.models.Product;
import com.example.springweave.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        // Logique métier : Un produit très cher doit être éligible au crédit par défaut
        if (product.getPrice().compareTo(new java.math.BigDecimal("50000")) > 0) {
            product.setCreditEligible(true);
        }
        return productRepository.save(product);
    }

    @Transactional
    public void updateStock(UUID productId, boolean available) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        product.setAvailable(available);
        productRepository.save(product);
    }
}