package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.treatment.CreateTreatmentDTO;
import com.example.medical.record.domain.dto.treatment.TreatmentDTO;
import com.example.medical.record.domain.entity.MedicalVisit;
import com.example.medical.record.domain.entity.Treatment;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.TreatmentRepository;
import com.example.medical.record.service.MedicalVisitService;
import com.example.medical.record.service.TreatmentService;
import com.example.medical.record.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;

    private final MedicalVisitService medicalVisitService;

    private final MapperUtil mapperUtil;

    @Override
    public List<TreatmentDTO> getAllTreatments() {
        List<Treatment> treatments = treatmentRepository.findAll();
        if (treatments.isEmpty()) {
            throw new ObjectNotFoundException("No treatments found");
        }
        return mapperUtil.mapList(treatments, TreatmentDTO.class);
    }

    @Override
    public TreatmentDTO getTreatmentById(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Treatment with id [%s] not found", id)));

        return mapperUtil.getModelMapper().map(treatment, TreatmentDTO.class);
    }

    @Override
    public ResponseEntity<TreatmentDTO> createTreatment(CreateTreatmentDTO createTreatmentDTO) {
        MedicalVisit medicalVisit = medicalVisitService.getMedicalVisit(createTreatmentDTO.getMedicalVisitId());

        Treatment
            treatment = mapperUtil.getModelMapper().map(createTreatmentDTO, Treatment.class);
        treatment.setMedicalVisit(medicalVisit);

        Treatment savedTreatment = treatmentRepository.save(treatment);

        TreatmentDTO
            treatmentDTO = mapperUtil.getModelMapper().map(savedTreatment, TreatmentDTO.class);

        return new ResponseEntity<>(treatmentDTO, HttpStatus.CREATED);
    }

    @Override
    public TreatmentDTO updateTreatment(Long id, CreateTreatmentDTO createTreatmentDTO) {
        Treatment treatment = treatmentRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Treatment with id [%s] not found", id)));

        mapperUtil.getModelMapper().map(createTreatmentDTO, treatment);
        Treatment updatedTreatment = treatmentRepository.save(treatment);
        return mapperUtil.getModelMapper().map(updatedTreatment, TreatmentDTO.class);
    }

    @Override
    public void deleteTreatment(Long treatmentId) {
        Treatment treatment = treatmentRepository.findById(treatmentId)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Treatment with id [%s] not found", treatmentId)));

        treatmentRepository.delete(treatment);
    }
}
