package com.example.springweave.repositories;

import com.example.springweave.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID; // Je suppose que AbstractBaseEntity utilise UUID comme Customer

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    // Pour vérifier si un email existe déjà avant de créer
    boolean existsByEmail(String email);

    // Pour retrouver un vendeur par son email
    Optional<Vendor> findByEmail(String email);

    // Très utile pour l'authentification API : retrouver le vendeur via sa clé
    Optional<Vendor> findByApiKey(String apiKey);
}