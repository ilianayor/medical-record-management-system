package com.example.medical.record.service;

import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.dto.patient.UpdatePatientDTO;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.domain.dto.patient.CreatePatientDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientService {

    List<PatientDTO> getAllPatients();

    PatientDTO getPatientById(Long patientId);

    ResponseEntity<PatientDTO> createPatient(CreatePatientDTO createPatientDTO);

    PatientDTO updatePatient(Long patientId, UpdatePatientDTO updatePatientDTO);

    void deletePatient(Long patientId);

    Patient getPatient(Long patientId);
}
