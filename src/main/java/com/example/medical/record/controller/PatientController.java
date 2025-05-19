package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.patient.CreatePatientDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.dto.patient.UpdatePatientDTO;
import com.example.medical.record.service.PatientService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @GetMapping("/{patientId}")
    public PatientDTO getPatient(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody CreatePatientDTO createPatientDTO) {
        return patientService.createPatient(createPatientDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @PutMapping("/{id}")
    public PatientDTO updatePatient(@PathVariable Long patientId, @RequestBody UpdatePatientDTO updatePatientDTO) {
        return patientService.updatePatient(patientId, updatePatientDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
