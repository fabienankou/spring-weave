package com.example.springweave.services;

import com.example.springweave.models.Product;
import com.example.springweave.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        // Logique métier : Un produit très cher doit être éligible au crédit par défaut
        if (product.getPrice() != null && product.getPrice().compareTo(new BigDecimal("50000")) > 0) {
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

    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit avec ID " + id + " non trouvé"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product updateProduct(UUID id, Product productDetails) {
        Product product = getProductById(id);
        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}