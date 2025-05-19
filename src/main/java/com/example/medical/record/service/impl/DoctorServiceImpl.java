package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.doctor.DoctorDTO;
import com.example.medical.record.domain.dto.doctor.GPPatientCountDTO;
import com.example.medical.record.domain.dto.doctor.UpdateDoctorDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.dto.specialty.CreateSpecialtyDTO;
import com.example.medical.record.domain.entity.Doctor;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.domain.entity.Role;
import com.example.medical.record.domain.entity.Specialty;
import com.example.medical.record.domain.entity.User;
import com.example.medical.record.exception.InvalidValidationException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.DoctorRepository;
import com.example.medical.record.repository.UserRepository;
import com.example.medical.record.domain.dto.doctor.CreateDoctorDTO;
import com.example.medical.record.domain.dto.doctor.DoctorVisitCountDTO;
import com.example.medical.record.service.DoctorService;
import com.example.medical.record.service.RoleService;
import com.example.medical.record.service.SpecialtyService;
import com.example.medical.record.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final SpecialtyService specialtyService;
    private final RoleService roleService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperUtil mapperUtil;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public DoctorDTO getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        return mapperUtil.getModelMapper().map(doctor, DoctorDTO.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return mapperUtil.mapList(doctors, DoctorDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<DoctorDTO> createDoctor(CreateDoctorDTO createDoctorDTO) {
        User user = new User();
        user.setUsername(createDoctorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createDoctorDTO.getPassword()));

        Role doctorRole = roleService.getRole("DOCTOR");
        user.setRoles(Set.of(doctorRole));

        userRepository.save(user);

        Doctor doctor = mapperUtil.getModelMapper().map(createDoctorDTO, Doctor.class);
        doctor.setUser(user);

        Doctor savedDoctor = doctorRepository.save(doctor);
        DoctorDTO doctorDTO = mapperUtil.getModelMapper().map(savedDoctor, DoctorDTO.class);

        return new ResponseEntity<>(doctorDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Override
    public DoctorDTO updateDoctor(Long doctorId, UpdateDoctorDTO updateDoctorDTO) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        mapperUtil.getModelMapper().map(updateDoctorDTO, doctor);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return mapperUtil.getModelMapper().map(updatedDoctor, DoctorDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        doctor.getSpecialties().clear();
        doctorRepository.delete(doctor);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Override
    @Transactional
    public List<CreateSpecialtyDTO> addSpecialtyToDoctor(Long doctorId, Long specialtyId) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        Specialty specialty = specialtyService.getSpecialty(specialtyId);
        Set<Specialty> specialties = doctor.getSpecialties();
        specialties.add(specialty);
        doctor.setSpecialties(specialties);

        doctorRepository.save(doctor);

        return doctor.getSpecialties().stream()
            .map(s -> mapperUtil.getModelMapper().map(s, CreateSpecialtyDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<DoctorVisitCountDTO> getVisitCountPerDoctor() {
        return doctorRepository.findDoctorVisitCounts().stream()
            .map(result -> new DoctorVisitCountDTO(
                (String) result[1],
                (Long) result[2],
                (Long) result[0]
            ))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<GPPatientCountDTO> getPatientCountPerGP() {
        return doctorRepository.findPatientCountPerGP().stream()
            .map(result -> new GPPatientCountDTO(
                (Long) result[0],
                (String) result[1],
                (Long) result[2]
            ))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<PatientDTO> getPatientsUnderGP(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        if (!doctor.getIsGp()) {
            throw new InvalidValidationException("Doctor is not a GP");
        }

        List<Patient> patients = doctorRepository.findPatientsByDoctorId(doctorId);
        return mapperUtil.mapList(patients, PatientDTO.class);
    }

    @Override
    public Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));
    }
}
