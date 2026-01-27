package com.example.springweave.repositories;

import com.example.springweave.models.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {

    // Pour le login : retrouver par nom d'utilisateur
    Optional<AdminUser> findByUsername(String username);
    Optional<AdminUser> findByemail(String email);

    // Vérifier l'unicité
    boolean existsByUsername(String username);

    // Si AbstractUserAccount contient l'email, tu peux aussi ajouter :
    // Optional<AdminUser> findByEmail(String email);
}