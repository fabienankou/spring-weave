package com.example.springweave.services;

import com.example.springweave.models.Notification;
import com.example.springweave.models.enums.*;
import com.example.springweave.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Async // Exécute l'envoi dans un thread séparé
    public void send(UUID userId, String recipient, String title, String message, NotificationChannel channel) {
        Notification notif = new Notification();
        notif.setUserId(userId);
        notif.setRecipient(recipient);
        notif.setTitle(title);
        notif.setMessage(message);
        notif.setChannel(channel);

        try {
            // Simulation d'envoi via un fournisseur (Twilio pour SMS, SendGrid pour Email)
            simulateExternalApiCall(channel, recipient, message);
            notif.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notif.setStatus(NotificationStatus.FAILED);
        }

        notificationRepository.save(notif);
    }

    private void simulateExternalApiCall(NotificationChannel channel, String to, String msg) {
        // Logique d'intégration API réelle
        System.out.println("Sending " + channel + " to " + to + " : " + msg);
    }
}