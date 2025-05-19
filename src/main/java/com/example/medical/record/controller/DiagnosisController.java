package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.diagnosis.DiagnosisDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @GetMapping
    public List<DiagnosisDTO> getAllDiagnoses() {
        return diagnosisService.getAllDiagnoses();
    }

    @GetMapping("/{id}")
    public DiagnosisDTO getDiagnosis(@PathVariable Long id) {
        return diagnosisService.getDiagnosisById(id);
    }

    @PostMapping
    public ResponseEntity<DiagnosisDTO> createDiagnosis(@Valid @RequestBody CreateDiagnosisDTO createDiagnosisDTO) {
        return diagnosisService.createDiagnosis(createDiagnosisDTO);
    }

    @PutMapping("/{id}")
    public DiagnosisDTO updateDiagnosis(@PathVariable Long id, @RequestBody CreateDiagnosisDTO createDiagnosisDTO) {
        return diagnosisService.updateDiagnosis(id, createDiagnosisDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteDiagnosis(@PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
    }

    @GetMapping("/most-frequent")
    public Map<String, Long> getMostFrequentlyDiagnosedIllnesses() {
        return diagnosisService.getMostFrequentlyDiagnosedIllnesses();
    }

    @GetMapping("/patients-with-diagnosis/{diagnosisId}")
    public List<PatientDTO> getPatientsByDiagnosisId(@PathVariable Long diagnosisId) {
        return diagnosisService.getPatientsByDiagnosisId(diagnosisId);
    }
}
