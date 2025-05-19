package com.example.medical.record.repository;

import com.example.medical.record.domain.entity.Diagnosis;
import com.example.medical.record.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    @Query("""
        SELECT d.diagnosisName, COUNT(mv) AS diagnosisCount
        FROM Diagnosis d
        JOIN d.medicalVisits mv
        GROUP BY d.diagnosisName
        HAVING COUNT(mv) = (
            SELECT MAX(subquery.diagnosisCount)
            FROM (
                SELECT COUNT(mv) AS diagnosisCount
                FROM Diagnosis d
                JOIN d.medicalVisits mv
                GROUP BY d.diagnosisName
            ) AS subquery
        )
        ORDER BY diagnosisCount DESC
        """)
    List<Object[]> findMostFrequentlyDiagnosedIllnesses();

    @Query("SELECT DISTINCT p FROM Patient p JOIN p.medicalVisits mv JOIN mv.diagnoses d WHERE d.id = :diagnosisId")
    List<Patient> findPatientsByDiagnosisId(@Param("diagnosisId") Long diagnosisId);

}

