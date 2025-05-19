package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.dto.patient.UpdatePatientDTO;
import com.example.medical.record.domain.entity.Doctor;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.domain.entity.Role;
import com.example.medical.record.domain.entity.User;
import com.example.medical.record.exception.InvalidDoctorAssignmentException;
import com.example.medical.record.exception.ObjectAlreadyExistsException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.UserRepository;
import com.example.medical.record.domain.dto.patient.CreatePatientDTO;
import com.example.medical.record.repository.PatientRepository;
import com.example.medical.record.service.DoctorService;
import com.example.medical.record.service.PatientService;
import com.example.medical.record.service.RoleService;
import com.example.medical.record.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    private final DoctorService doctorService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MapperUtil mapperUtil;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @Override
    public PatientDTO getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new ObjectNotFoundException("Patient not found"));

        return mapperUtil.getModelMapper().map(patient, PatientDTO.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        if (patients.isEmpty()) {
            throw new ObjectNotFoundException("Patients not found");
        }
        return mapperUtil.mapList(patients, PatientDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<PatientDTO> createPatient(CreatePatientDTO createPatientDTO) {
        if (patientRepository.existsByUcn(createPatientDTO.getUcn())) {
            throw new ObjectAlreadyExistsException(
                String.format("Patient with UCN %s already exists", createPatientDTO.getUcn()));
        }
        Doctor doctor = doctorService.getDoctor(createPatientDTO.getDoctorId());

        if (!doctor.getIsGp()) {
            throw new InvalidDoctorAssignmentException("Doctor is not a GP", createPatientDTO.getDoctorId());
        }

        User user = new User();
        user.setUsername(createPatientDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createPatientDTO.getPassword()));

        Role patientRole = roleService.getRole("PATIENT");
        user.setRoles(Set.of(patientRole));

        userRepository.save(user);

        Patient patient = mapperUtil.getModelMapper().map(createPatientDTO, Patient.class);
        patient.setDoctor(doctor);
        patient.setUser(user);

        Patient savedPatient = patientRepository.save(patient);
        PatientDTO patientDTO = mapperUtil.getModelMapper().map(savedPatient, PatientDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(patientDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PATIENT') and #id == authentication.principal.getPatientId())")
    @Override
    public PatientDTO updatePatient(Long id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Patient with id [%s] not found.", id)));

        mapperUtil.getModelMapper().map(updatePatientDTO, patient);
        Patient updatedPatient = patientRepository.save(patient);
        return mapperUtil.getModelMapper().map(updatedPatient, PatientDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Patient with id [%s] not found.", id)));

        patientRepository.deleteById(id);
    }

    @Override
    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
            .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Patient with id [%s] not found.", patientId)));
    }
}
