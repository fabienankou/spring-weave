package com.example.springweave.repositories;

import com.example.springweave.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    Page<Customer> findByIsActive(boolean isActive, Pageable pageable);
}