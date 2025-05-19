package com.example.medical.record.repository;

import com.example.medical.record.domain.entity.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
    @Query("SELECT mv FROM MedicalVisit mv JOIN mv.diagnoses d WHERE d.id = :diagnosisId")
    List<MedicalVisit> findMedicalVisitsByDiagnosisId(@Param("diagnosisId") Long diagnosisId);

    List<MedicalVisit> findByPatientId(Long patientId);

    List<MedicalVisit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    List<MedicalVisit> findByDoctorIdAndVisitDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);

}
