package com.example.medical.record.service;

import com.example.medical.record.domain.dto.treatment.CreateTreatmentDTO;
import com.example.medical.record.domain.dto.treatment.TreatmentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TreatmentService {
    List<TreatmentDTO> getAllTreatments();

    TreatmentDTO getTreatmentById(Long treatmentId);

    ResponseEntity<TreatmentDTO> createTreatment(CreateTreatmentDTO createTreatmentDTO);

    TreatmentDTO updateTreatment(Long treatmentId, CreateTreatmentDTO createTreatmentDTO);

    void deleteTreatment(Long treatmentId);
}
