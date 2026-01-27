package com.example.springweave.controllers;

import com.example.springweave.models.Vendor;
import com.example.springweave.repositories.VendorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors") // L'URL de base pour toutes les méthodes ci-dessous
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // GET : Récupérer tous les vendeurs
    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // POST : Créer un nouveau vendeur
    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        // Ici, tu pourrais ajouter des vérifications (ex: si l'email existe déjà)
        if (vendorRepository.existsByEmail(vendor.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Vendor savedVendor = vendorRepository.save(vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVendor);
    }

    // GET : Récupérer un vendeur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable UUID id) {
        return vendorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}