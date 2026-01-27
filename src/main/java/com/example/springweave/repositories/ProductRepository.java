package com.example.springweave.repositories;

import com.example.springweave.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Recherche paginée pour les clients
    Page<Product> findByCategoryAndIsAvailableTrue(String category, Pageable pageable);

    // Liste des produits d'un vendeur spécifique
    Page<Product> findByVendorId(UUID vendorId, Pageable pageable);
}