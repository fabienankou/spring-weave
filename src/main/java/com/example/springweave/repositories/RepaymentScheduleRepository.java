package com.example.springweave.repositories;

import com.example.springweave.models.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, UUID> {

    @Query("SELECT r FROM RepaymentSchedule r WHERE r.dueDate < :today AND r.status != 'PAID'")
    List<RepaymentSchedule> findOverdueInstallments(@Param("today") LocalDate today);
}