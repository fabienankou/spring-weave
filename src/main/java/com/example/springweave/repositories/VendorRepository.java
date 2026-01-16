package com.example.springweave.repositories;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    Optional<Vendor> findByApiKey(String apiKey);
}