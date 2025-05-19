package com.example.medical.record.service;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.diagnosis.DiagnosisDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.entity.Diagnosis;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DiagnosisService {
    List<DiagnosisDTO> getAllDiagnoses();

    DiagnosisDTO getDiagnosisById(Long id);

    ResponseEntity<DiagnosisDTO> createDiagnosis(CreateDiagnosisDTO createDiagnosisDTO);

    DiagnosisDTO updateDiagnosis(Long id, CreateDiagnosisDTO createDiagnosisDTO);

    void deleteDiagnosis(Long id);

    List<PatientDTO> getPatientsByDiagnosisId(Long diagnosisId);

    Map<String, Long> getMostFrequentlyDiagnosedIllnesses();

    Diagnosis getDiagnosis(Long diagnosisId);
}
