package com.example.springweave.models;

import com.example.springweave.models.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter @Setter
public class Notification extends AbstractBaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Peut être un Customer, Admin ou Vendor

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "is_read")
    private boolean isRead = false;

    private String recipient; // Email ou numéro de téléphone
}