package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.diagnosis.DiagnosisDTO;
import com.example.medical.record.domain.dto.patient.PatientDTO;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.DiagnosisRepository;
import com.example.medical.record.domain.entity.Diagnosis;
import com.example.medical.record.service.DiagnosisService;
import com.example.medical.record.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final MapperUtil mapperUtil;

    @Override
    public DiagnosisDTO getDiagnosisById(Long diagnosisId) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
            .orElseThrow(() -> new ObjectNotFoundException("Diagnosis not found"));

        return mapperUtil.getModelMapper().map(diagnosis, DiagnosisDTO.class);
    }

    @Override
    public List<DiagnosisDTO> getAllDiagnoses() {
        List<Diagnosis> diagnoses = diagnosisRepository.findAll();

        if (diagnoses.isEmpty()) {
            throw new ObjectNotFoundException("Diagnoses are not found");
        }

        return mapperUtil.mapList(diagnoses, DiagnosisDTO.class);
    }

    @Override
    public ResponseEntity<DiagnosisDTO> createDiagnosis(CreateDiagnosisDTO createDiagnosisDTO) {
        Diagnosis diagnosis = mapperUtil.getModelMapper().map(createDiagnosisDTO, Diagnosis.class);
        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);

        DiagnosisDTO diagnosisDTO = mapperUtil.getModelMapper().map(savedDiagnosis, DiagnosisDTO.class);
        return new ResponseEntity<>(diagnosisDTO, HttpStatus.CREATED);
    }

    @Override
    public DiagnosisDTO updateDiagnosis(Long id, CreateDiagnosisDTO createDiagnosisDTO) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Diagnosis not found"));

        mapperUtil.getModelMapper().map(createDiagnosisDTO, diagnosis);

        Diagnosis updatedDiagnosis = diagnosisRepository.save(diagnosis);
        return mapperUtil.getModelMapper().map(updatedDiagnosis, DiagnosisDTO.class);
    }

    @Override
    public void deleteDiagnosis(Long id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Diagnosis not found"));

        diagnosisRepository.deleteById(id);
    }

    @Override
    public List<PatientDTO> getPatientsByDiagnosisId(Long diagnosisId) {
        List<Patient> patients = diagnosisRepository.findPatientsByDiagnosisId(diagnosisId);

        return mapperUtil.mapList(patients, PatientDTO.class);
    }

    @Override
    public Map<String, Long> getMostFrequentlyDiagnosedIllnesses() {
        return diagnosisRepository.findMostFrequentlyDiagnosedIllnesses().stream()
            .collect(Collectors.toMap(
                result -> (String) result[0],
                result -> (Long) result[1]
            ));
    }

    @Override
    public Diagnosis getDiagnosis(Long diagnosisId) {
        return diagnosisRepository.findById(diagnosisId)
            .orElseThrow(() -> new ObjectNotFoundException("Diagnosis not found"));
    }
}
