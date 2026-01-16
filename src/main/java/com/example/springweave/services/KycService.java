package com.example.springweave.services;

import com.example.springweave.models.KycDocument;
import com.example.springweave.models.enums.KycStatus;
import com.example.springweave.repositories.KycDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycDocumentRepository kycDocumentRepository;

    @Transactional
    public KycDocument updateStatus(UUID documentId, KycStatus newStatus, String reason) {
        KycDocument doc = kycDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));

        doc.setVerificationStatus(newStatus);
        if (newStatus == KycStatus.REJECTED) {
            doc.setRejectionReason(reason);
            // Déclencher une notification ici
            return kycDocumentRepository.save(doc);
        } else {
            return kycDocumentRepository.save(doc);
        }

    }

    public String generateSafeUrl(KycDocument doc) {
        // Logique de Pre-signed URL S3 (expire après 15 min)
        return "https://s3.amazonaws.com/my-bucket/" + doc.getS3Url() + "?token=xyz";
    }
}