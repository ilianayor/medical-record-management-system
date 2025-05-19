package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.visit.CreateMedicalVisitDTO;
import com.example.medical.record.domain.dto.visit.MedicalVisitDTO;
import com.example.medical.record.domain.entity.Diagnosis;
import com.example.medical.record.domain.entity.Doctor;
import com.example.medical.record.domain.entity.MedicalVisit;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.exception.InvalidDateRangeException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.MedicalVisitRepository;
import com.example.medical.record.service.DiagnosisService;
import com.example.medical.record.service.DoctorService;
import com.example.medical.record.service.MedicalVisitService;
import com.example.medical.record.service.PatientService;
import com.example.medical.record.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalVisitServiceImpl implements MedicalVisitService {

    private final MedicalVisitRepository medicalVisitRepository;

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final DiagnosisService diagnosisService;

    private final MapperUtil mapperUtil;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public MedicalVisitDTO getMedicalVisitById(Long medicalVisitId) {
        MedicalVisit medicalVisit = medicalVisitRepository.findById(medicalVisitId)
            .orElseThrow(() -> new ObjectNotFoundException("Medical visit not found"));

        return mapperUtil.getModelMapper().map(medicalVisit, MedicalVisitDTO.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<MedicalVisitDTO> getAllMedicalVisits() {
        List<MedicalVisit> medicalVisits = medicalVisitRepository.findAll();

        if (medicalVisits.isEmpty()) {
            throw new ObjectNotFoundException("Medical visits not found");
        }
        return mapperUtil.mapList(medicalVisits, MedicalVisitDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public ResponseEntity<MedicalVisitDTO> createMedicalVisit(CreateMedicalVisitDTO createMedicalVisitDTO) {
        Doctor doctor = doctorService.getDoctor(createMedicalVisitDTO.getDoctorId());
        Patient patient = patientService.getPatient(createMedicalVisitDTO.getPatientId());

        MedicalVisit medicalVisit = mapperUtil.getModelMapper().map(createMedicalVisitDTO, MedicalVisit.class);
        medicalVisit.setPatient(patient);
        medicalVisit.setDoctor(doctor);

        MedicalVisit savedVisit = medicalVisitRepository.save(medicalVisit);
        MedicalVisitDTO medicalVisitDTO = mapperUtil.getModelMapper().map(savedVisit, MedicalVisitDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(medicalVisitDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('DOCTOR') and #createMedicalVisitDTO.getDoctorId() == authentication.principal.getDoctorId())")
    @Override
    public MedicalVisitDTO updateMedicalVisit(Long id, CreateMedicalVisitDTO createMedicalVisitDTO) {
        MedicalVisit medicalVisit = medicalVisitRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Medical visit not found."));

        mapperUtil.getModelMapper().map(createMedicalVisitDTO, medicalVisit);
        MedicalVisit updatedMedicalVisit = medicalVisitRepository.save(medicalVisit);
        return mapperUtil.getModelMapper().map(updatedMedicalVisit, MedicalVisitDTO.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteMedicalVisit(Long id) {
        MedicalVisit medicalVisit = medicalVisitRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Medical visit not found."));

        medicalVisitRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    @Transactional
    public List<CreateDiagnosisDTO> addDiagnosisToMedicalVisit(Long medicalVisitId, Long diagnosisId) {
        MedicalVisit medicalVisit = medicalVisitRepository.findById(medicalVisitId)
            .orElseThrow(() -> new ObjectNotFoundException("Medical visit not found"));

        Diagnosis diagnosis = diagnosisService.getDiagnosis(diagnosisId);

        Set<Diagnosis> diagnoses = medicalVisit.getDiagnoses();
        diagnoses.add(diagnosis);
        medicalVisit.setDiagnoses(diagnoses);

        medicalVisitRepository.save(medicalVisit);

        return medicalVisit.getDiagnoses().stream()
            .map(s -> mapperUtil.getModelMapper().map(s, CreateDiagnosisDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @Override
    public List<CreateMedicalVisitDTO> getVisitHistoryByPatient(Long patientId) {

        List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(patientId);

        if (visits.isEmpty()) {
            throw new ObjectNotFoundException("Visit history not found for patient");
        }

        return mapperUtil.mapList(visits, CreateMedicalVisitDTO.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<CreateMedicalVisitDTO> getVisitHistoryForAllDoctors(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }

        List<MedicalVisit> visits = medicalVisitRepository.findByVisitDateBetween(startDate, endDate);

        if (visits.isEmpty()) {
            throw new ObjectNotFoundException("There is no visit history");
        }

        return mapperUtil.mapList(visits, CreateMedicalVisitDTO.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @Override
    public List<CreateMedicalVisitDTO> getVisitHistoryForDoctor(Long doctorId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }

        List<MedicalVisit> visits =
            medicalVisitRepository.findByDoctorIdAndVisitDateBetween(doctorId, startDate, endDate);

        if (visits.isEmpty()) {
            throw new ObjectNotFoundException("Visit history not found for doctor within the given period");
        }

        return mapperUtil.mapList(visits, CreateMedicalVisitDTO.class);
    }

    @Override
    public MedicalVisit getMedicalVisit(Long medicalVisitId) {
        return medicalVisitRepository.findById(medicalVisitId)
            .orElseThrow(() -> new ObjectNotFoundException("Medical visit not found"));
    }
}
