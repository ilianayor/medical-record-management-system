package com.example.medical.record.repository;

import com.example.medical.record.domain.entity.InsurancePayment;
import com.example.medical.record.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsurancePaymentRepository extends JpaRepository<InsurancePayment, Long> {
    List<InsurancePayment> findByPatientId(Long patientId);

    List<InsurancePayment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    List<InsurancePayment> findByPatientAndPaymentDateAfter(Patient patient, LocalDate date);

    @Query("SELECT COUNT(h) > 0 FROM InsurancePayment h WHERE h.patient.id = :patientId AND h.paymentDate >= :sixMonthsAgo")

    boolean hasPaidHealthInsuranceInLastSixMonths(@Param("patientId") Long patientId,
                                                  @Param("sixMonthsAgo") LocalDate sixMonthsAgo);

}
