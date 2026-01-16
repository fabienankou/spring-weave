package com.example.springweave.services;

import com.example.springweave.models.Vendor;
import com.example.springweave.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public String rotateApiKey(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendeur introuvable"));

        // Génération d'une clé préfixée (ex: vk_...)
        String newKey = "vk_" + UUID.randomUUID().toString().replace("-", "");

        // On stocke la clé (idéalement hashée comme un password)
        vendor.setApiKey(newKey);
        vendorRepository.save(vendor);

        return newKey; // Retournée UNE SEULE FOIS au client
    }
}