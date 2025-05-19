package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.visit.CreateMedicalVisitDTO;
import com.example.medical.record.domain.dto.visit.MedicalVisitDTO;
import com.example.medical.record.service.MedicalVisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/medicalVisits")
@RequiredArgsConstructor
public class MedicalVisitController {

    private final MedicalVisitService medicalVisitService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping
    public List<MedicalVisitDTO> getAllMedicalVisits() {
        return medicalVisitService.getAllMedicalVisits();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/{id}")
    public MedicalVisitDTO getMedicalVisit(@PathVariable Long id) {
        return medicalVisitService.getMedicalVisitById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<MedicalVisitDTO> createMedicalVisit(
        @Valid @RequestBody CreateMedicalVisitDTO createMedicalVisitDTO) {
        return medicalVisitService.createMedicalVisit(createMedicalVisitDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('DOCTOR') and #createMedicalVisitDTO.getDoctorId() == authentication.principal.getDoctorId())")
    @PutMapping("/{id}")
    public MedicalVisitDTO updateMedicalVisit(@PathVariable Long id,
                                              @RequestBody CreateMedicalVisitDTO createMedicalVisitDTO) {

        return medicalVisitService.updateMedicalVisit(id, createMedicalVisitDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteMedicalVisit(@PathVariable Long id) {
        medicalVisitService.deleteMedicalVisit(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @PostMapping("/{medicalVisitId}/diagnoses/{diagnosisId}")
    public ResponseEntity<List<CreateDiagnosisDTO>> addDiagnosisToMedicalVisit(
        @PathVariable Long medicalVisitId,
        @PathVariable Long diagnosisId) {
        List<CreateDiagnosisDTO> updatedDiagnosis =
            medicalVisitService.addDiagnosisToMedicalVisit(medicalVisitId, diagnosisId);
        return ResponseEntity.ok(updatedDiagnosis);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @GetMapping("/history/patient/{patientId}")
    public ResponseEntity<List<CreateMedicalVisitDTO>> getVisitHistory(@PathVariable Long patientId) {
        List<CreateMedicalVisitDTO> visitHistory = medicalVisitService.getVisitHistoryByPatient(patientId);
        return ResponseEntity.ok(visitHistory);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/history")
    public ResponseEntity<List<CreateMedicalVisitDTO>> getVisitHistoryForAllDoctors(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate) {

        List<CreateMedicalVisitDTO> visitHistory = medicalVisitService.getVisitHistoryForAllDoctors(startDate, endDate);
        return ResponseEntity.ok(visitHistory);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/history/doctor/{doctorId}")
    public ResponseEntity<List<CreateMedicalVisitDTO>> getVisitHistoryForDoctor(
        @PathVariable Long doctorId,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate) {

        List<CreateMedicalVisitDTO> visitHistory = medicalVisitService.getVisitHistoryForDoctor(doctorId, startDate, endDate);
        return ResponseEntity.ok(visitHistory);
    }

}
