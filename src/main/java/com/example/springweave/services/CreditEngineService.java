package com.example.springweave.services;

import com.example.springweave.models.CreditApplication;
import com.example.springweave.models.RepaymentSchedule;
import com.example.springweave.models.enums.RepaymentStatus;
import com.example.springweave.repositories.CreditApplicationRepository;
import com.example.springweave.repositories.RepaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditEngineService {

    private final CreditApplicationRepository creditRepository;
    private final RepaymentScheduleRepository scheduleRepository;

    @Transactional
    public CreditApplication createCreditWithSchedule(CreditApplication application) {
        // 1. Calcul de la mensualité
        BigDecimal totalInterest = application.getAmount()
                .multiply(application.getInterestRate())
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);

        BigDecimal totalToRepay = application.getAmount().add(totalInterest);

        BigDecimal monthlyPayment = totalToRepay.divide(
                new BigDecimal(application.getDurationMonths()), RoundingMode.HALF_UP);

        application.setMonthlyPayment(monthlyPayment);


        CreditApplication savedCredit = creditRepository.save(application);


        List<RepaymentSchedule> schedules = new ArrayList<>();
        for (int i = 1; i <= savedCredit.getDurationMonths(); i++) {
            RepaymentSchedule schedule = RepaymentSchedule.builder()
                    .creditApplication(savedCredit)
                    .dueDate(LocalDate.now().plusMonths(i))
                    .totalAmount(monthlyPayment)
                    .status(RepaymentStatus.PENDING)
                    .lateFee(BigDecimal.ZERO)
                    .build();
            schedules.add(schedule);
        }

        scheduleRepository.saveAll(schedules);
        return savedCredit;
    }
}