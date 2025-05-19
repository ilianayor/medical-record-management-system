package com.example.medical.record.service;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.visit.CreateMedicalVisitDTO;
import com.example.medical.record.domain.dto.visit.MedicalVisitDTO;
import com.example.medical.record.domain.entity.MedicalVisit;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface MedicalVisitService {
    List<MedicalVisitDTO> getAllMedicalVisits();

    MedicalVisitDTO getMedicalVisitById(Long medicalVisitId);

    ResponseEntity<MedicalVisitDTO> createMedicalVisit(CreateMedicalVisitDTO createMedicalVisitDTO);

    MedicalVisitDTO updateMedicalVisit(Long medicalVisitId, CreateMedicalVisitDTO createMedicalVisitDTO);

    void deleteMedicalVisit(Long medicalVisitId);

    List<CreateDiagnosisDTO> addDiagnosisToMedicalVisit(Long medicalVisitId, Long diagnosisId);

    List<CreateMedicalVisitDTO> getVisitHistoryByPatient(Long patientId);

    List<CreateMedicalVisitDTO> getVisitHistoryForAllDoctors(LocalDate startDate, LocalDate endDate);

    List<CreateMedicalVisitDTO> getVisitHistoryForDoctor(Long doctorId, LocalDate startDate, LocalDate endDate);

    MedicalVisit getMedicalVisit(Long medicalVisitId);

}
