package com.example.medical.record.repository;

import com.example.medical.record.domain.entity.Doctor;
import com.example.medical.record.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d JOIN d.specialties s WHERE s.specialtyName = :specialtyName")
    List<Doctor> findBySpecialtiesName(@Param("specialtyName") String specialtyName);

    @Query("""
        SELECT d.id, d.doctorName, COUNT(mv) AS visitCount
        FROM Doctor d
        LEFT JOIN d.medicalVisits mv
        GROUP BY d.id, d.doctorName
        """)
    List<Object[]> findDoctorVisitCounts();

    @Query("""
            SELECT d.id AS doctor_id,
                   d.doctorName AS doctor_name,
                   COALESCE(COUNT(p.id), 0) AS patient_count
            FROM Doctor d
            LEFT JOIN Patient p ON p.doctor.id = d.id
            WHERE d.isGp = true
            GROUP BY d.id, d.doctorName
        """)
    List<Object[]> findPatientCountPerGP();

    @Query("SELECT p FROM Patient p WHERE p.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") Long doctorId);
}


