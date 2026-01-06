package com.example.springweave.services;

import com.example.springweave.models.RepaymentSchedule;
import com.example.springweave.models.enums.RepaymentStatus;
import com.example.springweave.repositories.RepaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Pour afficher des messages dans la console
public class CreditRiskScheduler {

    private final RepaymentScheduleRepository scheduleRepository;

    /**
     * S'exécute automatiquement tous les jours à minuit.
     * Cron expression : "0 0 0 * * ?" (Seconde Minute Heure Jour Mois Jour_Semaine)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void processOverduePayments() {
        log.info("Lancement de la vérification des impayés du : " + LocalDate.now());

        // 1. Récupérer toutes les échéances dont la date est passée et non payées
        List<RepaymentSchedule> overdueItems = scheduleRepository.findOverdueInstallments(LocalDate.now());

        for (RepaymentSchedule item : overdueItems) {
            // 2. Mettre à jour le statut en LATE (En retard)
            item.setStatus(RepaymentStatus.LATE);

            // 3. Appliquer une pénalité automatique (ex: 5% du montant dû)
            BigDecimal penalty = item.getTotalAmount().multiply(new BigDecimal("0.05"));
            item.setLateFee(item.getLateFee().add(penalty));

            // 4. Sauvegarder les modifications
            scheduleRepository.save(item);

            log.warn("Pénalité appliquée pour l'échéance ID: " + item.getId());
        }

        log.info("Fin du traitement. Nombre d'impayés traités : " + overdueItems.size());
    }
}