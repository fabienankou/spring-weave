package com.example.springweave.services;

import com.example.springweave.models.*;
import com.example.springweave.models.enums.*;
import com.example.springweave.repositories.CreditApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditApplicationRepository creditRepository;

    @Transactional
    public CreditApplication createApplication(Order order, int months) {
        CreditApplication app = new CreditApplication();
        app.setCustomer(order.getCustomer());
        app.setOrder(order);
        app.setAmount(order.getTotalAmount());
        app.setDurationMonths(months);
        app.setInterestRate(new BigDecimal("0.10")); // Taux fixe 10% pour l'exemple

        // Calcul simple : (Montant * (1 + Taux)) / Mois
        BigDecimal interest = app.getAmount().multiply(app.getInterestRate());
        app.setTotalToRepay(app.getAmount().add(interest));
        app.setMonthlyPayment(app.getTotalToRepay().divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP));

        return creditRepository.save(app);
    }

    @Transactional
    public void approveCredit(java.util.UUID applicationId) {
        CreditApplication app = creditRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        app.setStatus(CreditStatus.ACTIVE);

        // Génération de l'échéancier (1 paiement par mois)
        for (int i = 1; i <= app.getDurationMonths(); i++) {
            RepaymentSchedule schedule = new RepaymentSchedule();
            schedule.setCreditApplication(app);
            schedule.setInstallmentNumber(i);
            schedule.setDueDate(LocalDate.now().plusMonths(i));
            schedule.setAmountDue(app.getMonthlyPayment());
            app.getRepaymentSchedules().add(schedule);
        }

        creditRepository.save(app);
    }
}