package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.doctor.DoctorDTO;
import com.example.medical.record.domain.dto.doctor.GPPatientCountDTO;
import com.example.medical.record.domain.dto.doctor.UpdateDoctorDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.dto.specialty.CreateSpecialtyDTO;
import com.example.medical.record.domain.dto.doctor.CreateDoctorDTO;
import com.example.medical.record.domain.dto.doctor.DoctorVisitCountDTO;
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
import com.example.medical.record.service.DoctorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/{id}")
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody CreateDoctorDTO createDoctorDTO) {
        return doctorService.createDoctor(createDoctorDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public DoctorDTO updateDoctor(@RequestBody UpdateDoctorDTO doctor, @PathVariable Long id) {
        return doctorService.updateDoctor(id, doctor);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{doctorId}/specialties/{specialtyId}")
    public ResponseEntity<List<CreateSpecialtyDTO>> addSpecialtyToDoctor(
        @PathVariable Long doctorId,
        @PathVariable Long specialtyId) {

        List<CreateSpecialtyDTO> updatedSpecialties = doctorService.addSpecialtyToDoctor(doctorId, specialtyId);

        return ResponseEntity.ok(updatedSpecialties);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/visit-count")
    public List<DoctorVisitCountDTO> getVisitCountPerDoctor() {
        return doctorService.getVisitCountPerDoctor();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/patient-count")
    public List<GPPatientCountDTO> getPatientCountPerGP() {
        return doctorService.getPatientCountPerGP();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/{doctorId}/patients")
    public List<PatientDTO> getPatientsUnderGP(@PathVariable Long doctorId) {
        return doctorService.getPatientsUnderGP(doctorId);
    }

}
