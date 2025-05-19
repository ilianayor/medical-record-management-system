package com.example.medical.record.service;

import com.example.medical.record.domain.dto.doctor.DoctorDTO;
import com.example.medical.record.domain.dto.doctor.GPPatientCountDTO;
import com.example.medical.record.domain.dto.doctor.UpdateDoctorDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.dto.specialty.CreateSpecialtyDTO;
import com.example.medical.record.domain.entity.Doctor;
import com.example.medical.record.domain.dto.doctor.CreateDoctorDTO;
import com.example.medical.record.domain.dto.doctor.DoctorVisitCountDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();

    DoctorDTO getDoctorById(Long doctorId);

    ResponseEntity<DoctorDTO> createDoctor(CreateDoctorDTO createDoctorDTO);

    DoctorDTO updateDoctor(Long id, UpdateDoctorDTO updateDoctorDTO);

    void deleteDoctor(Long doctorId);

    List<CreateSpecialtyDTO> addSpecialtyToDoctor(Long doctorId, Long specialtyId);

    List<GPPatientCountDTO> getPatientCountPerGP();

    List<PatientDTO> getPatientsUnderGP(Long doctorId);

    List<DoctorVisitCountDTO> getVisitCountPerDoctor();

    Doctor getDoctor(Long doctorId);
}
