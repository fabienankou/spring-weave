package com.example.springweave.repositories;

import com.example.springweave.models.CreditApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CreditApplicationRepository extends JpaRepository<CreditApplication, UUID> {

    // Spring génère automatiquement les méthodes save(), findAll(), findById()...
}